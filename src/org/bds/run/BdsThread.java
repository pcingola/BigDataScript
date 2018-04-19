package org.bds.run;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import org.bds.Config;
import org.bds.data.Data;
import org.bds.executioner.Executioner;
import org.bds.executioner.Executioners;
import org.bds.lang.BdsNode;
import org.bds.lang.BdsNodeFactory;
import org.bds.lang.ProgramUnit;
import org.bds.lang.statement.BlockWithFile;
import org.bds.lang.statement.Statement;
import org.bds.lang.statement.StatementInclude;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.lang.value.ValueString;
import org.bds.osCmd.Exec;
import org.bds.report.Report;
import org.bds.scope.Scope;
import org.bds.task.Task;
import org.bds.task.TaskDependecies;
import org.bds.util.Gpr;
import org.bds.util.Timer;
import org.bds.vm.BdsVm;

/**
 * A threads used in a bigDataScript program
 *
 * It has all information to run a program (scope, pc, run state, etc)
 *
 * @author pcingola
 */
public class BdsThread extends Thread implements Serializable {

	private static final long serialVersionUID = 1206304272840188781L;

	public static final int MAX_TASK_FAILED_NAMES = 10; // Maximum number of failed tasks to show in summary
	public static final int FROZEN_SLEEP_TIME = 25; // Sleep time when frozen (milliseconds)
	private static int bdsThreadNumber = 1;

	Config config; // Config
	Random random; // Random number generator

	BdsVm vm; // Virtual machine

	// Program and state
	Statement statement; // Main statement executed by this thread
	RunState runState; // Latest RunState
	Value returnValue; // Latest return map (from a 'return' statement)
	int exitValue; // Exit map
	List<String> removeOnExit; // Files to be removed on exit
	Timer timer; // Program timer
	boolean freeze; // Freeze execution in next execution step

	// Debug stuff
	BufferedReader console; // Read debug commands from console
	DebugMode debugMode = null; // By default we are NOT debugging the program

	// BdsThread
	String currentDir; // Program's 'current directory'
	BdsThread parent; // Parent thread
	String bdsThreadId; // BdsThread ID
	int bdsThreadNum; // Thread number
	Map<String, BdsThread> bdsChildThreadsById; // Child threads

	// Task management
	TaskDependecies taskDependecies;
	List<Task> restoredTasks; // Unserialized tasks

	/**
	 * Get an ID for a node
	 */
	protected synchronized static int bdsThreadId() {
		return bdsThreadNumber++;
	}

	private BdsThread(BdsThread parent, Statement statement, Config config, BdsVm vm) {
		super();
		this.parent = parent;
		bdsThreadNum = bdsThreadId();
		setRunState(RunState.OK);
		this.config = config;
		random = new Random();
		removeOnExit = new LinkedList<>();
		taskDependecies = new TaskDependecies();
		bdsChildThreadsById = new HashMap<>();
		currentDir = System.getProperty(Exec.USER_DIR); // By default use Java program's current dir

		if (statement != null) setStatement(statement);

		taskDependecies.setVerbose(isVerbose());
		taskDependecies.setDebug(isDebug());

		if (vm != null) {
			this.vm = vm;
			vm.setBdsThread(this);
		}

		if (parent != null) parent.add(this);
	}

	public BdsThread(Statement statement, Config config, BdsVm vm) {
		this(null, statement, config, vm);
	}

	/**
	 * Add a child task
	 */
	public synchronized void add(BdsThread bdsThread) {
		bdsChildThreadsById.put(bdsThread.getBdsThreadId(), bdsThread);
	}

	/**
	 * Add a task
	 */
	public synchronized void add(Task task) {
		taskDependecies.add(task);
	}

	/**
	 * Has this or any child thread created tasks?
	 */
	public boolean anyTask() {
		if (!getTasks().isEmpty()) return true;

		for (BdsThread bdsThread : bdsChildThreadsById.values())
			if (bdsThread.anyTask()) return true;

		return false;
	}

	/**
	 * Create a checkpoint file
	 */
	public String checkpoint(BdsNode node) {
		// Skip checkpoint file?
		if (Config.get().isNoCheckpoint()) return "";

		// Create checkpoint
		String programFile = statement.getFileNameCanonical();
		String nodeFile = node.getFileNameCanonical();

		String checkpointFileName = programFile;
		if (!programFile.equals(nodeFile)) checkpointFileName += "." + Gpr.baseName(node.getFileName(), ".bds");
		checkpointFileName += ".line_" + node.getLineNum() + ".chp";

		return checkpoint(checkpointFileName);
	}

	//	/**
	//	 * Assertion failed (in bds test case)
	//	 */
	//	public void assertionFailed(BdsNode bdsnode, String message) {
	//		runState = RunState.FATAL_ERROR;
	//		String filePos = getFileLinePos(bdsnode);
	//		System.err.println("Assertion failed: " //
	//				+ filePos + (filePos.isEmpty() ? "" : ". ") //
	//				+ message //
	//		);
	//
	//		// Set exit map
	//		setExitValue(1L);
	//	}

	/**
	 * Create a checkpoint
	 */
	public String checkpoint(String checkpointFileName) {
		// Default file name
		if (checkpointFileName == null) checkpointFileName = statement.getFileNameCanonical() + ".chp";

		// Save
		if (isVerbose()) System.err.println("Creating checkpoint file: '" + checkpointFileName + "'");

		try {
			// Serialize root BdsThred to file
			ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(checkpointFileName)));
			BdsThread thRoot = getRoot();
			out.writeObject(thRoot);
			out.close();
		} catch (Exception e) {
			throw new RuntimeException("Error while serializing to file '" + checkpointFileName + "'", e);
		}

		return checkpointFileName;
	}

	void createBdsThreadId() {
		if (bdsThreadId != null) return; // Nothing to do

		// Create ID
		String name = Gpr.baseName(statement.getFileName());
		if (isRoot()) bdsThreadId = String.format("%s.%2$tY%2$tm%2$td_%2$tH%2$tM%2$tS_%2$tL", name, Calendar.getInstance());
		else bdsThreadId = parent.bdsThreadId + "_parallel_" + getId();
	}

	/**
	 * Create a dir for all log files
	 */
	public void createLogDir() {
		String dirname = getLogBaseName();
		File logdir = new File(dirname);
		if (!logdir.exists()) logdir.mkdirs();

		// No logging? Delete on exit
		if ((config != null) && !config.isLog()) logdir.deleteOnExit();
	}

	/**
	 * Create a new (and canonical) file relative to 'currentDir'
	 */
	public Data data(String fileName) {
		return Data.factory(fileName, currentDir);
	}

	/**
	 * Show a fatal error
	 */
	public void fatalError(BdsNode bdsnode, String message) {
		setRunState(RunState.FATAL_ERROR);
		String filePos = getFileLinePos(bdsnode);
		System.err.println("Fatal error: " //
				+ filePos + (filePos.isEmpty() ? "" : ". ") //
				+ message);

		// Show BDS stack trace
		try {
			System.err.println(stackTrace());
		} catch (Throwable t) {
			t.printStackTrace();
		}

		// Create checkpoint
		try {
			String checkpointFileName = checkpoint(bdsnode);
			if (checkpointFileName.isEmpty()) System.err.println("Creating checkpoint file: Config or command line option disabled checkpoint file creation, nothing done.");
			else System.err.println("Creating checkpoint file '" + checkpointFileName + "'");
		} catch (Throwable t) {
			// Ignore serialization error at this stage (we are within a fatal error)
		}

		// Set exit map
		setExitValue(1L);
	}

	/**
	 * Show a fatal error
	 */
	public void fatalError(BdsNode bdsnode, Throwable t) {
		if (getRunState().isFatalError()) return;
		fatalError(bdsnode, t.getMessage());

		// Show java stack trace
		if ((config == null) || isVerbose()) t.printStackTrace();
	}

	public void fatalError(String message) {
		fatalError(getBdsNodeCurrent(), message);
	}

	/**
	 * Fork: Create and start a new bds thread
	 */
	public BdsThread fork(BdsVm vmfork) {
		BdsThread newBdsThread = new BdsThread(this, statement, config, vmfork);

		push(new ValueString(newBdsThread.getBdsThreadId())); // Parent process: return child's thread ID
		newBdsThread.push(new ValueString("")); // Fork returns empty string on child process

		newBdsThread.start();
		return newBdsThread;
	}

	//	/**
	//	 * Running in debug mode: This method is invoked right before running 'node'
	//	 */
	//	void debug(BdsNode node) {
	//		if (node.isStopDebug()) {
	//			switch (debugMode) {
	//			case RUN:
	//				// Keep running until we find a breakpoint
	//				break;
	//
	//			case STEP:
	//				// Show options
	//				debugStep(node);
	//				break;
	//
	//			case STEP_OVER:
	//				// Run until we are back from a function (method) call
	//				if (debugStepOverPc == null) debugStep(node);
	//				else if (pc.size() <= debugStepOverPc.size()) {
	//					// Are we done stepping over?
	//					debugStep(node);
	//					debugStepOverPc = null;
	//				}
	//				break;
	//
	//			default:
	//				throw new RuntimeException("Unimplemented debug mode: " + debugMode);
	//			}
	//		}
	//	}

	//	/**
	//	 * Show debug 'step' options
	//	 */
	//	void debugStep(BdsNode node) {
	//		// Show current line
	//		String prg = node.toString();
	//		if (prg.indexOf("\n") > 0) prg = "\n" + Gpr.prependEachLine("\t", prg);
	//		else prg = prg + " ";
	//
	//		String prompt = "DEBUG [" + debugMode + "]: " //
	//				+ node.getFileName() //
	//				+ ", line " + node.getLineNum() //
	//				+ (isDebug() ? " (" + node.getClass().getSimpleName() + ")" : "") //
	//				+ ": " + prg //
	//				+ "> " //
	//		;
	//
	//		//---
	//		// Wait for options
	//		//---
	//		while (true) {
	//			System.err.print(prompt);
	//			String line = readConsole();
	//
	//			if (line == null) return;
	//			line = line.trim();
	//
	//			// Parse options
	//			if (line.isEmpty()) {
	//				// Empty line? => Continue using the same debug mode
	//				if (debugMode == DebugMode.STEP_OVER) debugUpdatePc(node);
	//				return;
	//			} else if (line.equalsIgnoreCase("h")) {
	//				// Show help
	//				System.err.println("Help:");
	//				System.err.println("\t[RETURN]  : " + (debugMode == DebugMode.STEP_OVER ? "step over" : "step"));
	//				System.err.println("\tf         : show current Frame (variables within current scope)");
	//				System.err.println("\th         : Help");
	//				System.err.println("\to         : step Over");
	//				System.err.println("\tp         : show Progeam counter");
	//				System.err.println("\tr         : Run program (until next breakpoint)");
	//				System.err.println("\ts         : Step");
	//				System.err.println("\tt         : show stack Trace");
	//				System.err.println("\tv varname : show Variable 'varname'");
	//				System.err.println("");
	//			} else if (line.equalsIgnoreCase("f")) {
	//				// Show current 'frame'
	//				System.err.println(getScope().toString(false));
	//			} else if (line.equalsIgnoreCase("p")) {
	//				// Show current 'frame'
	//				System.err.println(getPc());
	//			} else if (line.equalsIgnoreCase("o")) {
	//				// Switch to 'STEP_OVER' mode
	//				debugMode = DebugMode.STEP_OVER;
	//				debugUpdatePc(node);
	//				return;
	//			} else if (line.equalsIgnoreCase("r")) {
	//				// Switch to 'RUN' mode
	//				debugMode = DebugMode.RUN;
	//				return;
	//			} else if (line.equalsIgnoreCase("s")) {
	//				// Switch to 'STEP' mode
	//				debugMode = DebugMode.STEP;
	//				return;
	//			} else if (line.equalsIgnoreCase("t")) {
	//				// Show stack trace
	//				System.err.println(this.stackTrace());
	//			} else if (line.startsWith("v ")) {
	//				// Get variable's name
	//				String varName = line.substring(2).trim(); // Remove leading "s " string
	//
	//				// Get and show variable
	//				Value val = getScope().getValue(varName);
	//				if (val == null) System.err.println("Variable '" + varName + "' not found");
	//				else System.err.println(val.getType() + " : " + val);
	//			} else {
	//				System.err.println("Unknown command '" + line + "'");
	//			}
	//		}
	//	}
	//
	//	/**
	//	 * Do we need to update 'step over' reference PC
	//	 */
	//	void debugUpdatePc(BdsNode node) {
	//		if (debugStepOverPc == null //
	//				&& debugMode == DebugMode.STEP_OVER // Is it in 'step over' mode?
	//				&& (node instanceof FunctionCall || node instanceof MethodCall) // Is it a function or method call?
	//		) {
	//			debugStepOverPc = new ProgramCounter(pc);
	//		}
	//	}

	//	/**
	//	 * Freeze thread execution
	//	 */
	//	protected void freeze() {
	//		RunState oldRunState = runState;
	//		setRunState(RunState.FROZEN);
	//
	//		while (freeze) {
	//			try {
	//				Thread.sleep(FROZEN_SLEEP_TIME);
	//				if (isDebug()) Gpr.debug("Frozen bdsThread: '" + getBdsThreadId() + "'");
	//			} catch (InterruptedException e) {
	//				// Nothing to do
	//			}
	//		}
	//		setRunState(oldRunState); // Restore old state
	//	}

	/**
	 * Get node currently executed by the VM
	 */
	public BdsNode getBdsNodeCurrent() {
		int nodeId = vm.getNodeId();
		return BdsNodeFactory.get().getNode(nodeId);
	}

	public String getBdsThreadId() {
		return bdsThreadId;
	}

	public List<BdsThread> getBdsThreads() {
		List<BdsThread> list = new ArrayList<>();
		list.addAll(bdsChildThreadsById.values());
		return list;
	}

	/**
	 * Get all child threads
	 */
	public List<BdsThread> getBdsThreadsAll() {
		List<BdsThread> list = new ArrayList<>();
		list.add(this);

		for (BdsThread bth : bdsChildThreadsById.values())
			list.addAll(bth.getBdsThreadsAll());

		return list;
	}

	/**
	 * Get variable's map as a bool
	 */
	public boolean getBool(String varName) {
		return getScope().getValue(varName).asBool();
	}

	public Config getConfig() {
		return config;
	}

	public String getCurrentDir() {
		return currentDir;
	}

	public DebugMode getDebugMode() {
		return debugMode;
	}

	public int getExitValue() {
		return exitValue;
	}

	/**
	 * Try to get file / line / pos information
	 * Recurse to parent node if not found
	 */
	public String getFileLinePos(BdsNode bdsNode) {

		// If the node has file/line info, we are done
		if (bdsNode.getFileNameCanonical() != null) { //
			return bdsNode.getFileName() //
					+ ", line " + bdsNode.getLineNum() //
					+ ", pos " + (bdsNode.getCharPosInLine() + 1) //
			;
		}

		// TODO: FIX THIS USING VM CALL STACK

		// 		Map<Integer, BdsNode> nodesById = getNodesById();
		//
		//		// No file/line info in 'bdsNode'. we walk the program-counter
		//		// form end to start and return the information from the
		//		// first node that has file/line data
		//
		//		// Go backwards on stack and return the first information for
		//		// the first node that has file/line/pos info.
		//		for (int idx = pc.size() - 1; idx >= 0; idx--) {
		//			int nodeId = pc.nodeId(idx);
		//			BdsNode bn = nodesById.get(nodeId);
		//
		//			if (bn.getFileNameCanonical() != null) { //
		//				return bn.getFileName() + ", line " + bn.getLineNum();
		//			}
		//		}

		// Nothing found? Return empty
		return "";
	}

	/**
	 * Get variable's map as an int
	 */
	public long getInt(String varName) {
		return getScope().getValue(varName).asInt();
	}

	public String getLogBaseName() {
		return bdsThreadId;
	}

	/**
	 * Map nodes by ID
	 */
	Map<Integer, BdsNode> getNodesById() {
		List<BdsNode> bdsNodes = statement.findNodes(null, true, false);
		Map<Integer, BdsNode> nodesById = new HashMap<>();
		for (BdsNode n : bdsNodes)
			nodesById.put(n.getId(), n);

		return nodesById;
	}

	public BdsThread getParent() {
		return parent;
	}

	public ProgramUnit getProgramUnit() {
		return (ProgramUnit) statement;
	}

	public Random getRandom() {
		return random;
	}

	/**
	 * Get variable's map as a real
	 */
	public double getReal(String varName) {
		return getScope().getValue(varName).asReal();
	}

	public Value getReturnValue() {
		return returnValue;
	}

	/**
	 * Get 'root' thread
	 */
	public BdsThread getRoot() {
		if (parent == null) return this;
		return parent.getRoot();
	}

	public RunState getRunState() {
		return runState;
	}

	public Scope getScope() {
		return vm.getScope();
	}

	public Statement getStatement() {
		return statement;
	}

	/**
	 * Get variable's map as a string
	 */
	public String getString(String varName) {
		return getScope().getValue(varName).toString();
	}

	/**
	 * Get a task (this thread or any child thread)
	 */
	public Task getTask(String taskId) {
		Task task = taskDependecies.getTask(taskId);
		if (task != null) return task;
		for (BdsThread bdsThread : bdsChildThreadsById.values()) {
			task = bdsThread.getTask(taskId);
			if (task != null) return task;
		}
		return null;
	}

	/**
	 * Get all tasks
	 */
	public Collection<Task> getTasks() {
		return taskDependecies.getTasks();
	}

	/**
	 * Get a thread
	 */
	public synchronized BdsThread getThread(String threadId) {
		// Do we have this thread?
		BdsThread bdsth = bdsChildThreadsById.get(threadId);
		if (bdsth != null) return bdsth;

		// Search in all child threads
		for (BdsThread child : bdsChildThreadsById.values()) {
			bdsth = child.getThread(threadId);
			if (bdsth != null) return bdsth;
		}

		// Not found
		return null;
	}

	public Timer getTimer() {
		return timer;
	}

	/**
	 * Get variable's value (as a Value object)
	 */
	public Value getValue(String varName) {
		return getScope().getValue(varName);
	}

	/**
	 * Execute dependency tasks to achieve goal 'out'
	 */
	public synchronized List<String> goal(String out) {
		List<String> taskIds = new ArrayList<>();

		// Find dependencies
		Set<Task> tasks = taskDependecies.goal(this, out);

		// No update needed?
		if (tasks == null) {
			if (isDebug()) Gpr.debug("Goal '" + out + "' has no dependent tasks. Nothing to do.");
			return taskIds;
		}

		// Convert to task IDs
		for (Task t : tasks)
			taskIds.add(t.getId());

		return taskIds;
	}

	/**
	 * Does 'varName' exists?
	 */
	public boolean hasVariable(String varName) {
		return getScope().getValue(varName) != null;
	}

	public boolean isDebug() {
		return config != null && config.isDebug();
	}

	/**
	 * Is this the 'root' (main) thread?
	 */
	public boolean isRoot() {
		return parent == null;
	}

	public boolean isThreadsDone() {
		return bdsChildThreadsById.isEmpty();
	}

	public boolean isVerbose() {
		return config != null && config.isVerbose();
	}

	/**
	 * Kill: Stop execution of current thread
	 */
	public void kill() {
		setRunState(RunState.THREAD_KILLED); // Set state to 'kill'
	}

	/**
	 * Kill one task/thread
	 */
	public void kill(Value vtaskId) {
		String taskId = vtaskId.asString();
		if (taskId == null) return;

		// Is 'tid' a bdsThread ID?
		if (bdsChildThreadsById.containsKey(taskId)) {
			// Kill thread
			bdsChildThreadsById.get(taskId).kill();
		} else {
			// Kill task: Just send a kill to all Executioners
			for (Executioner executioner : Executioners.getInstance().getAll())
				executioner.kill(taskId);
		}
	}

	/**
	 * Kill all tasks/threads in the list
	 */
	public void kill(ValueList taskIds) {
		if (taskIds == null) return;

		// We are done when ALL tasks are done
		for (Value tid : taskIds)
			kill(tid);
	}

	public Value pop() {
		return vm.pop();
	}

	public void print() {
		// Create a list with program file and all included files
		List<BdsNode> nodeWithFiles = statement.findNodes(StatementInclude.class, true, false);
		nodeWithFiles.add(0, statement);

		// Show code
		for (BdsNode bwf : nodeWithFiles) {
			System.out.println("Program file: '" + bwf.getFileName() + "'");
			printCode(((BlockWithFile) bwf).getFileText());
		}

		// Show stack trace
		System.out.println("Stack trace:");
		System.out.println(stackTrace());

		// Show scopes
		for (Scope scope = getScope(); scope != null; scope = scope.getParent()) {
			if (!scope.isEmpty()) {
				BdsNode node = scope.getNode();

				String scopeInfo = "";
				if ((node != null) && (scope.getNode().getFileName() != null)) scopeInfo = scope.getNode().getFileName() + ":" + scope.getNode().getLineNum();
				System.out.println("--- Scope: " + scopeInfo + " ---");

				for (String varName : scope) {
					System.out.println(scope.getValue(varName));
				}
				System.out.println("--- End of scope ---");
			}
		}
	}

	void printCode(String code) {
		// Show file contents
		int lineNum = 1;
		for (String line : code.split("\n"))
			System.out.println(String.format("%6d |%s", lineNum++, line));
		System.out.println("");
	}

	public void push(Value val) {
		vm.push(val);
	}

	/**
	 * Read a line from STDIN
	 */
	public String readConsole() {
		try {
			if (console == null) console = new BufferedReader(new InputStreamReader(System.in));
			String line = console.readLine();
			return line;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Remove a child thread
	 */
	public void remove(BdsThread bdsThread) {
		bdsChildThreadsById.remove(bdsThread.getBdsThreadId());
	}

	/**
	 * Remove stale files (controlled by '-noRmOnExit' command line option)
	 */
	void removeStaleData() {
		if (!isRoot()) return;

		// Remove all pending files
		if (!removeOnExit.isEmpty()) {
			if (config != null && config.isNoRmOnExit()) {
				if (isDebug()) Timer.showStdErr("\tDeleting stale files: Cancelled ('noRmOnExit' is active).");
			} else {
				if (isDebug()) Timer.showStdErr("Deleting stale files:");
				for (String fileName : removeOnExit) {
					if (isVerbose()) System.err.println("\t" + fileName);
					Data.factory(fileName).delete();
				}
			}
		}
	}

	/**
	 * Send task from un-serialization to execution list
	 */
	public void restoreUnserializedTasks() {
		if (restoredTasks == null) return;
		for (Task task : restoredTasks) {
			add(task);

			if ((!task.isDone() // Not finished?
					|| (task.isFailed() && !task.isCanFail())) // or finished but 'can fail'?
					&& !task.isDependency() // Don't execute dependencies, unledd needed
			) {
				// Task not finished or failed? Re-execute
				// TODO: FIXME!!!
				// ExpressionTask.execute(this, task);
				throw new RuntimeException("!!!! IMPLEMENT");
			}
		}

		restoredTasks = null; // We don't need it any more (plus we want to make sure we don't schedule tasks more than once)
	}

	public synchronized void rmOnExit(Value vfile) {
		String file = vfile.asString();
		Data data = data(file);

		// Add file for removal
		removeOnExit.add(data.getPath());

		// Remove local (cached) copy of the file
		if (data.isRemote() && (data.getLocalPath() != null)) removeOnExit.add(data.getLocalPath());
	}

	public synchronized void rmOnExit(ValueList files) {
		for (Value v : files)
			rmOnExit(v);
	}

	@Override
	public void run() {
		timer = new Timer();

		createLogDir(); // Create log dir

		// Start child threads (e.g. when recovering)
		for (BdsThread bth : bdsChildThreadsById.values()) {
			if (!bth.isAlive() && !bth.getRunState().isFinished()) bth.start();
		}

		// Add this thread to collections
		BdsThreads.getInstance().add(this);

		// Run statement (i.e. run program)
		boolean ok = true;
		runStatement();

		// We are done running
		if (isDebug()) Timer.showStdErr("BdsThread finished: " + getBdsThreadId());
		if (getRunState().isFatalError()) {
			// Error condition
			ok = false;
		} else {
			// OK, we finished running
			if (isDebug()) Timer.showStdErr((isRoot() ? "Program" : "Parallel") + " '" + getBdsThreadId() + "' execution finished");

			// Implicit 'wait' statement at the end of the program (only if the program finished 'naturally')
			ok = waitAll();
		}

		// All tasks in wait finished OK?
		if (!ok) {
			// Errors? Then set exit status appropriately
			exitValue = 1;
		} else {
			switch (getRunState()) {
			case FATAL_ERROR:
			case THREAD_KILLED:
				exitValue = 1;
				break;

			default:
				// Do nothing with exitValue;
				break;
			}
		}

		// We are completely done
		setRunState(RunState.FINISHED);

		// Finish up
		removeStaleData();
		timer.end();

		// Create reports? Only root thread creates reports
		if (config != null && isRoot()) {
			// Create HTML report?
			if (config.isReportHtml()) {
				Report report = new Report(this, false);
				report.createReport();
			}

			// Create YAML report?
			if (config.isReportYaml()) {
				Report report = new Report(this, true);
				report.createReport();
			}
		}

		if (!isRoot()) parent.remove(this); // Remove this bdsThread from parent's threads

		// OK, we are done
		if (isDebug()) {
			// Root thread? Report all tasks
			TaskDependecies td = isRoot() ? TaskDependecies.get() : taskDependecies;

			Timer.showStdErr((isRoot() ? "Program" : "Parallel") + " " //
					+ "'" + getBdsThreadId() + "'" //
					+ " finished" //
					+ (isDebug() ? ", run state: '" + getRunState() + "'" : "") //
					+ ", exit map: " + getExitValue() //
					+ ", tasks executed: " + td.getTasks().size() //
					+ ", tasks failed: " + td.countTaskFailed() //
					+ ", tasks failed names: " + td.taskFailedNames(MAX_TASK_FAILED_NAMES, " , ") //
					+ "." //
			);
		}

		// Remove thread from "running threads"
		BdsThreads.getInstance().remove();
	}

	/**
	 * Run statements (i.e. run program)
	 */
	protected void runStatement() {
		try {
			vm.run();
		} catch (Throwable t) {
			setRunState(RunState.FATAL_ERROR);
			if (isVerbose()) throw new RuntimeException("Fatal error", t);
			else Timer.showStdErr("Fatal error: Program execution finished");
		}
	}

	/**
	 * Check that stack has size zero (perform this check after execution finishes)
	 */
	public void sanityCheckStack() {
		vm.sanityCheckStack();
	}

	public void setCurrentDir(String currentDir) {
		this.currentDir = currentDir;
	}

	public void setDebugMode(DebugMode debugMode) {
		this.debugMode = debugMode;
	}

	public void setExitValue(long exitValue) {
		this.exitValue = (int) exitValue;
	}

	/**
	 * Freeze execution before next node run
	 */
	public void setFreeze(boolean freeze) {
		this.freeze = freeze;
	}

	public void setRandomSeed(long seed) {
		random = new Random(seed);
	}

	public void setReturnValue(Value returnValue) {
		this.returnValue = returnValue;
	}

	public void setRunState(RunState runState) {
		this.runState = runState;

		if (vm != null) {
			switch (runState) {

			case OK:
				vm.setRun(false);
				break;

			case FATAL_ERROR:
			case FINISHED:
			case THREAD_KILLED:
				vm.setRun(false);
				break;

			case WAIT_RECOVER:
				throw new RuntimeException("!!!");

			default:
				break;
			}
		}
	}

	//	public void setScope(Scope scope) {
	//		this.scope = scope;
	//	}

	/**
	 * Set program unit and update bigDataScriptThreadId"
	 */
	public void setStatement(Statement statement) {
		this.statement = statement;
		createBdsThreadId(); // Create thread ID based on program's name
	}

	/**
	 * Show BDS calling stack
	 */
	public String stackTrace() {
		return vm.stackTrace();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("BigDataScriptThread: " + bdsThreadNum + "\n");
		sb.append("\tRun state : " + getRunState() + "\n");
		sb.append("\tProgram   :\n" + statement.toStringTree("\t\t", "program") + "\n");
		return sb.toString();
	}

	/**
	 * Wait for one task/thread to finish
	 */
	public boolean wait(String id) {
		if (id == null) return true;

		// Note: We could be waiting for another thread's taskID.
		//       So we need to wait on the global TaskDependencies
		if (TaskDependecies.get().hasTask(id)) return TaskDependecies.get().waitTask(id);

		// Note: We could be waiting for a non-child thread to finish
		//       So we have to wait on the 'root' BdsThread'
		BdsThread bdsThRoot = getRoot();
		BdsThread bdsTh = bdsThRoot.getThread(id);
		if (bdsTh != null) return waitThread(bdsTh);
		return true; // Nothing to do (already finished)
	}

	/**
	 * Wait for all tasks/threads in the list to finish
	 * @return true if all tasks/threads finished OK (or failed but were allowed to fail, i.e. canFail = true)
	 */
	public boolean wait(ValueList ids) {
		if (ids == null) return true;

		boolean ok = true;

		// We are done when ALL tasks/threads are done
		for (Value id : ids)
			ok &= wait(id.toString());

		return ok;
	}

	public boolean waitAll() {
		boolean ok = taskDependecies.waitTasksAll();
		ok &= waitThreadAll();
		return ok;
	}

	/**
	 * Wait for one thread to finish
	 * @return true if thread finished OK
	 */
	public boolean waitThread(BdsThread bdsThread) {
		try {
			if (bdsThread != null) {
				if (isDebug()) Timer.showStdErr("Waiting for parallel '" + bdsThread.getBdsThreadId() + "' to finish. RunState: " + bdsThread.getRunState());
				if (bdsThread.getRunState().isFinished()) return true;
				bdsThread.join();
				return bdsThread.getExitValue() == 0; // Finished OK?
			}
		} catch (InterruptedException e) {
			if (isVerbose()) e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean waitThreadAll() {
		// Wait for all tasks to finish
		boolean ok = true;

		if (isDebug() && !isThreadsDone()) Timer.showStdErr("Waiting for all 'parrallel' to finish.");

		// Populate a list of threads to avoid concurrent modification
		List<BdsThread> bdsThreads = new LinkedList<>();
		bdsThreads.addAll(bdsChildThreadsById.values());

		// Wait for all threads
		for (BdsThread bdsth : bdsThreads)
			ok &= waitThread(bdsth);

		return ok;
	}

}
