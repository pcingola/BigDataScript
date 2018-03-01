package org.bds.run;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bds.Config;
import org.bds.data.Data;
import org.bds.executioner.Executioner;
import org.bds.executioner.Executioners;
import org.bds.lang.BdsNode;
import org.bds.lang.ProgramUnit;
import org.bds.lang.expression.ExpressionTask;
import org.bds.lang.statement.BlockWithFile;
import org.bds.lang.statement.Checkpoint;
import org.bds.lang.statement.FunctionCall;
import org.bds.lang.statement.MethodCall;
import org.bds.lang.statement.Statement;
import org.bds.lang.statement.StatementInclude;
import org.bds.lang.statement.Wait;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueList;
import org.bds.lang.value.ValueReal;
import org.bds.lang.value.ValueString;
import org.bds.osCmd.Exec;
import org.bds.report.Report;
import org.bds.scope.GlobalScope;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;
import org.bds.serialize.BdsSerialize;
import org.bds.serialize.BdsSerializer;
import org.bds.task.Task;
import org.bds.task.TaskDependecies;
import org.bds.util.Gpr;
import org.bds.util.Timer;

/**
 * A threads used in a bigDataScript program
 *
 * It has all information to run a program (scope, pc, run state, etc)
 *
 * @author pcingola
 */
public class BdsThread extends Thread implements BdsSerialize {

	public static final int MAX_TASK_FAILED_NAMES = 10; // Maximum number of failed tasks to show in summary
	public static final int FROZEN_SLEEP_TIME = 25; // Sleep time when frozen (milliseconds)
	private static int threadNumber = 1;

	Config config; // Config
	Random random; // Random number generator

	// Program and state
	Statement statement; // Main statement executed by this thread
	String statementNodeId; // Statement's ID, used only when un-serializing
	ProgramCounter pc; // Program counter
	RunState runState; // Latest RunState
	Value returnValue; // Latest return map (from a 'return' statement)
	int exitValue; // Exit map
	List<String> removeOnExit; // Files to be removed on exit
	Timer timer; // Program timer
	boolean freeze; // Freeze execution in next execution step

	// Debug stuff
	BufferedReader console; // Read debug commands from console
	DebugMode debugMode = null; // By default we are NOT debugging the program
	ProgramCounter debugStepOverPc;

	// Scope
	Scope scope; // Base scope
	String scopeNodeId; // Scope's ID, used only when un-serializing
	Deque<Value> stack; // Program stack

	// BdsThread
	String currentDir; // Program's 'current directory'
	BdsThread parent; // Parent thread
	String bdsThreadId; // BdsThread ID
	int bdsThreadNum; // Thread number
	Map<String, BdsThread> bdsChildThreadsById; // Child threads

	// Task management
	TaskDependecies taskDependecies;
	List<Task> restoredTasks; // Unserialized tasks.

	/**
	 * Get an ID for a node
	 */
	protected synchronized static int bigDataScriptThreadId() {
		return threadNumber++;
	}

	public BdsThread(Statement statement, BdsThread parent) {
		super();
		this.parent = parent;
		bdsThreadNum = bigDataScriptThreadId();
		pc = new ProgramCounter(parent.getPc());
		scope = parent.scope;
		stack = new LinkedList<>();
		runState = RunState.OK;
		config = parent.config;
		random = parent.random;
		removeOnExit = parent.removeOnExit;
		currentDir = parent.currentDir;

		bdsChildThreadsById = new HashMap<>();
		taskDependecies = new TaskDependecies();

		setStatement(statement);
		parent.add(this);

		taskDependecies.setVerbose(isVerbose());
		taskDependecies.setDebug(isDebug());
	}

	public BdsThread(Statement statement, Config config) {
		super();
		bdsThreadNum = bigDataScriptThreadId();
		pc = new ProgramCounter();
		scope = GlobalScope.get();
		stack = new LinkedList<>();
		runState = RunState.OK;
		this.config = config;
		random = new Random();
		removeOnExit = new LinkedList<>();
		taskDependecies = new TaskDependecies();
		bdsChildThreadsById = new HashMap<>();
		currentDir = System.getProperty(Exec.USER_DIR); // By default use Java program's current dir

		if (statement != null) setStatement(statement);

		taskDependecies.setVerbose(isVerbose());
		taskDependecies.setDebug(isDebug());
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
	 * Add tasks form un-serialization
	 */
	public void addUnserialized(Task task) {
		if (restoredTasks == null) restoredTasks = new ArrayList<>();
		restoredTasks.add(task);
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
	 * Assertion failed (in bds test case)
	 */
	public void assertionFailed(BdsNode bdsnode, String message) {
		runState = RunState.FATAL_ERROR;
		String filePos = getFileLinePos(bdsnode);
		System.err.println("Assertion failed: " //
				+ filePos + (filePos.isEmpty() ? "" : ". ") //
				+ message //
		);

		// Set exit map
		setExitValue(1L);
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

	/**
	 * Create a checkpoint
	 */
	public String checkpoint(String checkpointFileName) {
		// Default file name
		if (checkpointFileName == null) checkpointFileName = statement.getFileNameCanonical() + ".chp";

		// Save
		if (isVerbose()) System.err.println("Creating checkpoint file: '" + checkpointFileName + "'");
		BdsSerializer bdsSer = new BdsSerializer(checkpointFileName, config);
		bdsSer.save(getRoot()); // Save root thread

		return checkpointFileName;
	}

	/**
	 * Make sure that the statement node is the first in the checkpoint recovery
	 */
	public void checkpointRecoverReset() {
		if (pc.checkpointRecoverReset(statement)) return;

		// Empty PC means that we finished executing
		if (!pc.isEmpty()) {
			// If PC is not empty, we should have found the nodes
			throw new RuntimeException("Checkpoint statement not found in Program Counter:" + this);
		}
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
	 * Running in debug mode: This method is invoked right before running 'node'
	 */
	void debug(BdsNode node) {
		if (node.isStopDebug()) {
			switch (debugMode) {
			case RUN:
				// Keep running until we find a breakpoint
				break;

			case STEP:
				// Show options
				debugStep(node);
				break;

			case STEP_OVER:
				// Run until we are back from a function (method) call
				if (debugStepOverPc == null) debugStep(node);
				else if (pc.size() <= debugStepOverPc.size()) {
					// Are we done stepping over?
					debugStep(node);
					debugStepOverPc = null;
				}
				break;

			default:
				throw new RuntimeException("Unimplemented debug mode: " + debugMode);
			}
		}
	}

	/**
	 * Show debug 'step' options
	 */
	void debugStep(BdsNode node) {
		// Show current line
		String prg = node.toString();
		if (prg.indexOf("\n") > 0) prg = "\n" + Gpr.prependEachLine("\t", prg);
		else prg = prg + " ";

		String prompt = "DEBUG [" + debugMode + "]: " //
				+ node.getFileName() //
				+ ", line " + node.getLineNum() //
				+ (isDebug() ? " (" + node.getClass().getSimpleName() + ")" : "") //
				+ ": " + prg //
				+ "> " //
		;

		//---
		// Wait for options
		//---
		while (true) {
			System.err.print(prompt);
			String line = readConsole();

			if (line == null) return;
			line = line.trim();

			// Parse options
			if (line.isEmpty()) {
				// Empty line? => Continue using the same debug mode
				if (debugMode == DebugMode.STEP_OVER) debugUpdatePc(node);
				return;
			} else if (line.equalsIgnoreCase("h")) {
				// Show help
				System.err.println("Help:");
				System.err.println("\t[RETURN]  : " + (debugMode == DebugMode.STEP_OVER ? "step over" : "step"));
				System.err.println("\tf         : show current Frame (variables within current scope)");
				System.err.println("\th         : Help");
				System.err.println("\to         : step Over");
				System.err.println("\tp         : show Progeam counter");
				System.err.println("\tr         : Run program (until next breakpoint)");
				System.err.println("\ts         : Step");
				System.err.println("\tt         : show stack Trace");
				System.err.println("\tv varname : show Variable 'varname'");
				System.err.println("");
			} else if (line.equalsIgnoreCase("f")) {
				// Show current 'frame'
				System.err.println(getScope().toString(false));
			} else if (line.equalsIgnoreCase("p")) {
				// Show current 'frame'
				System.err.println(getPc());
			} else if (line.equalsIgnoreCase("o")) {
				// Switch to 'STEP_OVER' mode
				debugMode = DebugMode.STEP_OVER;
				debugUpdatePc(node);
				return;
			} else if (line.equalsIgnoreCase("r")) {
				// Switch to 'RUN' mode
				debugMode = DebugMode.RUN;
				return;
			} else if (line.equalsIgnoreCase("s")) {
				// Switch to 'STEP' mode
				debugMode = DebugMode.STEP;
				return;
			} else if (line.equalsIgnoreCase("t")) {
				// Show stack trace
				System.err.println(this.stackTrace());
			} else if (line.startsWith("v ")) {
				// Get variable's name
				String varName = line.substring(2).trim(); // Remove leading "s " string

				// Get and show variable
				ScopeSymbol ss = getScope().getSymbol(varName);
				if (ss == null) System.err.println("Variable '" + varName + "' not found");
				else System.err.println(ss.getType() + " : " + ss.getValue());
			} else {
				System.err.println("Unknown command '" + line + "'");
			}
		}
	}

	/**
	 * Do we need to update 'step over' reference PC
	 */
	void debugUpdatePc(BdsNode node) {
		if (debugStepOverPc == null //
				&& debugMode == DebugMode.STEP_OVER // Is it in 'step over' mode?
				&& (node instanceof FunctionCall || node instanceof MethodCall) // Is it a function or method call?
		) {
			debugStepOverPc = new ProgramCounter(pc);
		}
	}

	/**
	 * Show a fatal error
	 */
	public void fatalError(BdsNode bdsnode, String message) {
		runState = RunState.FATAL_ERROR;
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
		String checkpointFileName = checkpoint(bdsnode);
		if (checkpointFileName.isEmpty()) System.err.println("Creating checkpoint file: Config or command line option disabled checkpoint file creation, nothing done.");
		else System.err.println("Creating checkpoint file '" + checkpointFileName + "'");

		// Set exit map
		setExitValue(1L);
	}

	/**
	 * Show a fatal error
	 */
	public void fatalError(BdsNode bdsnode, Throwable t) {
		if (runState == RunState.FATAL_ERROR) return;
		fatalError(bdsnode, t.getMessage());

		// Show java stack trace
		if ((config == null) || isVerbose()) t.printStackTrace();
	}

	/**
	 * Freeze thread execution
	 */
	protected void freeze() {
		RunState oldRunState = runState;
		runState = RunState.FROZEN;

		while (freeze) {
			try {
				Thread.sleep(FROZEN_SLEEP_TIME);
				if (isDebug()) Gpr.debug("Frozen bdsThread: '" + getBdsThreadId() + "'");
			} catch (InterruptedException e) {
				// Nothing to do
			}
		}

		runState = oldRunState; // Restore old state
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
		return (Boolean) getScope().getSymbol(varName).getValue();
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

		// No file/line info in 'bdsNode'. we walk the program-counter
		// form end to start and return the information from the
		// first node that has file/line data

		// Find all nodes and add them to a map (by nodeId)
		List<BdsNode> bdsNodes = statement.findNodes(null, true);
		Map<Integer, BdsNode> nodesById = new HashMap<>();
		for (BdsNode n : bdsNodes)
			nodesById.put(n.getId(), n);

		// Go backwards on PC and return the first information for
		// the first node that has file/line/pos info.
		for (int idx = pc.size() - 1; idx >= 0; idx--) {
			int nodeId = pc.nodeId(idx);
			BdsNode bn = nodesById.get(nodeId);

			if (bn.getFileNameCanonical() != null) { //
				return bn.getFileName() + ", line " + bn.getLineNum();
			}
		}

		// Nothing found? Return empty
		return "";
	}

	/**
	 * Get variable's map as an int
	 */
	public long getInt(String varName) {
		ScopeSymbol ssym = getScope().getSymbol(varName);
		return (Long) ssym.getValue();
	}

	public String getLogBaseName() {
		return bdsThreadId;
	}

	@Override
	public String getNodeId() {
		return bdsThreadId;
	}

	/**
	 * Get variable's map as a java object
	 */
	public Object getObject(String varName) {
		return getScope().getSymbol(varName).getValue();
	}

	public BdsThread getParent() {
		return parent;
	}

	public ProgramCounter getPc() {
		return pc;
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
		return (Double) getScope().getSymbol(varName).getValue();
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
		return scope;
	}

	public String getScopeNodeId() {
		return scopeNodeId;
	}

	public Deque<Value> getStack() {
		return stack;
	}

	public Statement getStatement() {
		return statement;
	}

	public String getStatementNodeId() {
		return statementNodeId;
	}

	/**
	 * Get variable's map as a string
	 */
	public String getString(String varName) {
		return getScope().getSymbol(varName).getValue().toString();
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
		return getScope().getSymbol(varName) != null;
	}

	/**
	 * Are we in CHECKPOINT_RECOVER mode?
	 */
	public boolean isCheckpointRecover() {
		return runState.isCheckpointRecover();
	}

	public boolean isDebug() {
		return config != null && config.isDebug();
	}

	public boolean isExit() {
		return runState.isExit();
	}

	public boolean isFatalError() {
		return runState.isFatalError();
	}

	public boolean isFinished() {
		return runState.isFinished();
	}

	/**
	 * Is this thread frozen?
	 */
	public boolean isFrozen() {
		return runState.isFrozen();
	}

	public boolean isReturn() {
		return runState.isReturn();
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
		runState = RunState.THREAD_KILLED; // Set state to 'kill'
	}

	/**
	 * Kill all tasks/threads in the list
	 */
	@SuppressWarnings("rawtypes")
	public void kill(List taskIds) {
		if (taskIds == null) return;

		// We are done when ALL tasks are done
		for (Object tid : taskIds)
			kill(tid.toString());
	}

	/**
	 * Kill one task/thread
	 */
	public void kill(String taskId) {
		if (taskId == null) return;

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
	 * Create a new scope
	 */
	public void newScope(BdsNode node) {
		scope = new Scope(scope, node);
	}

	/**
	 * Back to old scope
	 */
	public void oldScope() {
		scope = scope.getParent();
	}

	public Value peek() {
		if (isCheckpointRecover()) return null;
		return stack.peek();
	}

	public Value pop() {
		if (isCheckpointRecover()) return null;
		return stack.removeFirst();
	}

	/**
	 * Pop a bool from stack
	 */
	public boolean popBool() {
		return (Boolean) Types.BOOL.cast(pop()).get();
	}

	/**
	 * Pop an int from stack
	 */
	public long popInt() {
		return (Long) Types.INT.cast(pop()).get();
	}

	/**
	 * Pop a real from stack
	 */
	public double popReal() {
		return (Double) Types.REAL.cast(pop()).get();
	}

	/**
	 * Pop a string from stack
	 */
	public String popString() {
		return (String) Types.STRING.cast(pop()).get();
	}

	public void print() {
		// Create a list with program file and all included files
		List<BdsNode> nodeWithFiles = statement.findNodes(StatementInclude.class, true);
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
					System.out.println(scope.getSymbol(varName));
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

	public void push(boolean b) {
		push(new ValueBool(b));
	}

	public void push(double v) {
		push(new ValueReal(v));
	}

	public void push(List<String> ls) {
		ValueList vl = new ValueList(Types.STRING);
		vl.set(ls);
		push(vl);
	}

	public void push(long v) {
		push(new ValueInt(v));
	}

	public void push(String s) {
		push(new ValueString(s));
	}

	public void push(Value obj) {
		if (!isCheckpointRecover()) stack.addFirst(obj);
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
		bdsChildThreadsById.remove(bdsThread);
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
				ExpressionTask.execute(this, task);
			}
		}

		restoredTasks = null; // We don't need it any more (plus we want to make sure we don't schedule tasks more than once)
	}

	@SuppressWarnings("rawtypes")
	public synchronized void rmOnExit(List files) {
		for (Object o : files)
			rmOnExit(o.toString());
	}

	public synchronized void rmOnExit(String file) {
		Data data = data(file);

		// Add file for removal
		removeOnExit.add(data.getPath());

		// Remove local (cached) copy of the file
		if (data.isRemote() && (data.getLocalPath() != null)) removeOnExit.add(data.getLocalPath());
	}

	@Override
	public void run() {
		timer = new Timer();

		createLogDir(); // Create log dir

		// Start child threads (e.g. when recovering)
		for (BdsThread bth : bdsChildThreadsById.values()) {
			if (!bth.isAlive() && !bth.isFinished()) bth.start();
		}

		// Add this thread to collections
		BdsThreads.getInstance().add(this);

		// Run statement (i.e. run program)
		boolean ok = true;
		runStatement();

		// We are done running
		if (isDebug()) Timer.showStdErr("BdsThread finished: " + getBdsThreadId());
		if (isFatalError()) {
			// Error condition
			ok = false;
		} else {
			// OK, we finished running
			if (isDebug()) Timer.showStdErr((isRoot() ? "Program" : "Parallel") + " '" + getBdsThreadId() + "' execution finished");

			// Implicit 'wait' statement at the end of the program (only if the program finished 'naturally')
			if (!isFatalError() && !isExit()) ok = waitAll();
			else ok = false;
		}

		// All tasks in wait finished OK?
		if (!ok) {
			// Errors? Then set exit status appropriately
			exitValue = 1;
		} else {
			Object ev = null;

			switch (runState) {
			case EXIT:
				ev = getExitValue();
				break;

			case RETURN:
				ev = getReturnValue();
				break;

			case FATAL_ERROR:
			case THREAD_KILLED:
				ev = 1L;
				break;

			default:
				ev = null;
				break;
			}

			if (ev != null && ev instanceof Long) exitValue = (int) ((long) ((Long) ev)); // Yes, it's a very weird cast....
		}

		// We are completely done
		runState = RunState.FINISHED;

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
					+ (isDebug() ? ", run state: '" + runState + "'" : "") //
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
	 * Run this node
	 */
	public void run(BdsNode node) {
		// Before node execution
		if (!isCheckpointRecover()) runBegin(node);

		// Should we freeze execution?
		if (freeze) freeze();

		try {
			// Run?
			if (shouldRun(node)) {
				// Debug mode?
				if (debugMode != null) debug(node);

				// Run node
				node.runStep(this);
			}
		} catch (Throwable t) {
			fatalError(node, t);
		}

		// After node execution
		if (!isCheckpointRecover()) runEnd(node);
	}

	/**
	 * Run before running the node
	 */
	protected void runBegin(BdsNode node) {
		// Need a new scope?
		if (node.isNeedsScope()) newScope(node);
		getPc().push(node);
	}

	/**
	 * Run after running the node
	 */
	protected void runEnd(BdsNode node) {
		getPc().pop(node);

		// Restore old scope?
		if (node.isNeedsScope()) oldScope();
	}

	/**
	 * Run statements (i.e. run program)
	 */
	protected void runStatement() {
		try {
			if (isCheckpointRecover() && pc.isEmpty()) {
				// This is a special case: We are in recovery mode, but the
				// thread has finished execution (PC is empty)
				// = >There is nothing to execute
				if (isDebug()) Gpr.debug("Thread finished execution before checkpoint, nothing to run. BdsThreadId: " + getBdsThreadId());
			} else {
				// Normal execution => Run statement
				run(statement);
			}
		} catch (Throwable t) {
			runState = RunState.FATAL_ERROR;
			if (isVerbose()) throw new RuntimeException(t);
			else Timer.showStdErr("Fatal error: Program execution finished");
		}
	}

	/**
	 * Check that stack has size zero (perform this check after execution finishes)
	 */
	public void sanityCheckStack() {
		if (stack.size() > 0) {
			Gpr.debug("Stack size: " + stack.size() + "\n" + toStringStack());
			throw new RuntimeException("Inconsistent stack. Size: " + stack.size());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void serializeParse(BdsSerializer serializer) {
		bdsThreadNum = (int) serializer.getNextFieldInt();
		removeOnExit = serializer.getNextFieldList(TypeList.get(Types.STRING));
		bdsThreadId = serializer.getNextFieldString();
		statementNodeId = serializer.getNextFieldString();
		scopeNodeId = serializer.getNextFieldString();

		// Update global thread number?
		if (threadNumber < bdsThreadNum) threadNumber = bdsThreadNum + 1;

		// Set parent thread
		String parentBdsThreadId = serializer.getNextFieldString();
		if (!parentBdsThreadId.isEmpty()) {
			parent = serializer.getBdsThread(parentBdsThreadId);
			parent.add(this);
		}

		// State
		runState = RunState.valueOf(serializer.getNextFieldString());

		// Current dir
		currentDir = serializer.getNextFieldString();

		// Stack
		String b64 = serializer.getNextField();
		stack = (b64 != null && !b64.isEmpty() ? (Deque<Value>) serializer.base64Decode(b64) : null);
	}

	@Override
	public String serializeSave(BdsSerializer serializer) {
		// This 'serializeSave' method can be called form another thread
		// We have to make sure that the thread is not running while
		// serializing (otherwise we'll recover an inconsistent state)

		// Set this thread to freeze (it will be frozen in the next 'run' call)
		setFreeze(true);
		String pcOld = pc.toString(); // Save current program counter

		// Serialize
		String serStr = serializeSaveAll(serializer);

		// Has program counter changed?
		String pcNew = pc.toString();
		if (!pcNew.equals(pcOld)) {
			// PC changed => We have to serialize again
			// This time we are 'safe' because thread should be frozen
			serStr = serializeSaveAll(serializer);
		}

		// Un-freeze
		setFreeze(false);

		return serStr.toString();
	}

	/**
	 * Serialize main and data
	 */
	public String serializeSaveAll(BdsSerializer serializer) {
		StringBuilder out = new StringBuilder();
		out.append(serializeSaveThreadMain(serializer));
		out.append("\n");
		out.append(serializeSaveThreadData(serializer));
		return out.toString();
	}

	/**
	 * Save thread's data
	 */
	protected String serializeSaveThreadData(BdsSerializer serializer) {
		StringBuilder out = new StringBuilder();

		// Save program counter
		out.append(serializer.serializeSave(pc));

		// Save scopes
		out.append(serializer.serializeSave(scope));

		// Save program nodes
		out.append(serializer.serializeSave(statement));

		// Save all tasks (in the same order that they were added)
		for (Task task : taskDependecies.getTasks())
			out.append(serializer.serializeSave(task));

		// Save all threads
		for (BdsThread bdsTh : bdsChildThreadsById.values())
			out.append(serializer.serializeSave(bdsTh));

		return out.toString();
	}

	/**
	 * Save thread's main information
	 */
	protected String serializeSaveThreadMain(BdsSerializer serializer) {
		StringBuilder out = new StringBuilder();

		out.append(getClass().getSimpleName());
		out.append("\t" + bdsThreadNum);
		out.append("\t" + serializer.serializeSaveValue(removeOnExit));
		out.append("\t" + serializer.serializeSaveValue(getBdsThreadId()));
		out.append("\t" + serializer.serializeSaveValue(statement.getNodeId()));
		out.append("\t" + serializer.serializeSaveValue(scope.getNodeId()));
		out.append("\t" + serializer.serializeSaveValue(parent != null ? parent.getBdsThreadId() : ""));
		out.append("\t" + serializer.serializeSaveValue(runState.toString()));
		out.append("\t" + serializer.serializeSaveValue(currentDir));
		out.append("\t" + serializer.base64encode(stack));
		return out.toString();
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

	public void setPc(ProgramCounter pc) {
		this.pc = pc;
	}

	public void setRandomSeed(long seed) {
		random = new Random(seed);
	}

	public void setReturnValue(Value returnValue) {
		this.returnValue = returnValue;
	}

	public void setRunState(RunState runState) {
		this.runState = runState;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	/**
	 * Find and set statement
	 */
	public void setStatement(Map<String, BdsSerialize> nodesById) {
		Statement stat = (Statement) nodesById.get(statementNodeId);
		if (stat == null) throw new RuntimeException("Cannot find statement node '" + statementNodeId + "'");
		setStatement(stat);
	}

	/**
	 * Set program unit and update bigDataScriptThreadId"
	 */
	public void setStatement(Statement statement) {
		this.statement = statement;
		statementNodeId = statement.getNodeId();
		createBdsThreadId(); // Create thread ID based on program's name
	}

	/**
	 * Should we run this node?
	 */
	public boolean shouldRun(BdsNode node) {
		// Should we run?
		switch (runState) {
		case OK:
		case BREAK:
		case CONTINUE:
		case RETURN:
			return true;

		case EXIT:
		case FATAL_ERROR:
		case THREAD_KILLED:
			return false;

		case WAIT_RECOVER:
		case CHECKPOINT_RECOVER:
			break;

		default:
			throw new RuntimeException("Unhandled RunState: " + runState);
		}

		//---
		// Recovering form a checkpoint
		//---

		// Which node are we looking for?
		if (!pc.checkpointRecoverHasNextNode()) {
			// No more nodes to recover? This might happen when recovering a thread that already finished execution
			return false;
		}

		// Match?
		int nodeNum = pc.checkpointRecoverNextNode();
		if (node.getId() == nodeNum) {
			// Node found!
			pc.checkpointRecoverFound();

			// More nodes to recurse? => continue
			if (pc.checkpointRecoverHasNextNode()) return true;

			// Last node found!
			runState = RunState.OK; // Switch to 'normal' run state
			if (node instanceof Wait) runState = RunState.WAIT_RECOVER; // We want to recover all tasks that failed in wait statement
			if (node instanceof Checkpoint) return false; // We want to recover AFTER the checkpoint statement
			return true;
		}

		return false;
	}

	/**
	 * Show BDS calling stack
	 */
	public String stackTrace() {

		// Get all nodes and hash them by ID
		List<BdsNode> nodes = statement.findNodes(null, true);
		HashMap<Integer, BdsNode> nodesById = new HashMap<>();
		for (BdsNode node : nodes) {
			nodesById.put(node.getId(), node);
		}
		nodesById.put(statement.getId(), statement);

		// Collect source code
		HashMap<String, String[]> fileName2codeLines = new HashMap<>();
		for (BdsNode node : nodesById.values()) {
			if (node instanceof BlockWithFile) {
				BlockWithFile bwf = (BlockWithFile) node;
				String fileName = node.getFileNameCanonical();
				String code = bwf.getFileText();
				if (fileName != null && code != null) fileName2codeLines.put(fileName, code.split("\n"));
			}
		}

		// Show stack
		StringBuilder sb = new StringBuilder();
		String linePrev = "";
		for (int nodeId : pc) {
			BdsNode node = nodesById.get(nodeId);
			if (node == null) continue;

			String fileName = node.getFileName();
			if (fileName == null) continue;

			String[] codeLines = fileName2codeLines.get(fileName);
			if (codeLines == null) continue;

			int lineNum = node.getLineNum() - 1;
			if (lineNum <= 0 || lineNum >= codeLines.length) continue;

			String line = Gpr.baseName(node.getFileName()) + ", line " + node.getLineNum() + " :\t" + codeLines[lineNum] + "\n";
			if (!line.equals(linePrev)) sb.append(line);
			linePrev = line;
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("BigDataScriptThread: " + bdsThreadNum + "\n");
		sb.append("\tPC        : " + pc + "\n");
		sb.append("\tRun state : " + runState + "\n");
		sb.append("\tStack     : " + stack + "\n");
		sb.append("\tScope     : " + scope + "\n");
		sb.append("\tProgram   :\n" + statement.toStringTree("\t\t", "program") + "\n");
		return sb.toString();
	}

	/**
	 * Show stack
	 */
	public String toStringStack() {
		StringBuilder sb = new StringBuilder();
		int num = 0;
		for (Object obj : stack)
			sb.append("Stack[" + (num++) + "]:\tClass: " + obj.getClass().getSimpleName() + "\tValue: " + obj.toString() + "\n");
		return sb.toString();
	}

	/**
	 * Wait for all tasks/threads in the list to finish
	 * @return true if all tasks/threads finished OK or it were allowed to fail (i.e. canFail = true)
	 */
	@SuppressWarnings("rawtypes")
	public boolean wait(List ids) {
		if (ids == null) return true;

		boolean ok = true;

		// We are done when ALL tasks/threads are done
		for (Object id : ids)
			ok &= wait(id.toString());

		return ok;
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
				if (bdsThread.isFinished()) return true;
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
		for (BdsThread bdsth : bdsChildThreadsById.values())
			ok &= waitThread(bdsth);

		return ok;
	}

}
