package ca.mcgill.mcb.pcingola.bigDataScript.run;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript;
import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioner;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioners;
import ca.mcgill.mcb.pcingola.bigDataScript.htmlTemplate.RTemplate;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNode;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.BlockWithFile;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Checkpoint;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.ExpressionTask;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.ProgramUnit;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.StatementInclude;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Wait;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerialize;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.task.TailFile;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * A threads used in a bigDataScript program
 *
 * It has all information to run a program (scope, pc, run state, etc)
 *
 * @author pcingola
 */
public class BigDataScriptThread extends Thread implements BigDataScriptSerialize {

	public static final int SLEEP_TIME = 250;
	private static int threadNumber = 1;
	public static String REPORT_TEMPLATE = "SummaryTemplate.html";
	public static final String REPORT_RED_COLOR = "style=\"background-color: #ffc0c0\"";
	public static final int REPORT_TIMELINE_HEIGHT = 42; // Size of time-line element (life, universe and everything)
	public static final String LINE = "--------------------";

	String bigDataScriptThreadId;
	int bigDataScriptThreadNum;
	int checkPointRecoverNodeIdx;
	int exitValue;
	ProgramCounter pc;
	Scope scope;
	ProgramUnit programUnit;
	RunState runState;
	Object returnValue;
	ArrayList<Task> tasks; // Sorted list of tasks (need it for serialization purposes)
	HashMap<String, Task> tasksById;
	Config config;
	Random random;
	List<String> removeOnExit;
	ArrayList<Task> restoredTasks; // Unserialized tasks.
	Timer timer;
	TaskDependecies taskDependecies;

	/**
	 * Get an ID for a node
	 */
	protected synchronized static int bigDataScriptThreadId() {
		return threadNumber++;
	}

	public BigDataScriptThread(ProgramUnit programUnit, Config config) {
		super();
		bigDataScriptThreadNum = bigDataScriptThreadId();
		pc = new ProgramCounter();
		scope = Scope.getGlobalScope();
		runState = RunState.OK;
		tasks = new ArrayList<Task>();
		tasksById = new HashMap<String, Task>();
		this.config = config;
		random = new Random();
		removeOnExit = new LinkedList<String>();
		taskDependecies = new TaskDependecies();

		if (programUnit != null) setProgram(programUnit);
	}

	/**
	 * Add a task
	 */
	public void add(Task task) {
		if (tasksById.containsKey(task.getId())) return; // Don't add a task twice

		tasks.add(task);
		tasksById.put(task.getId(), task);
		taskDependecies.add(task);
	}

	/**
	 * Add dependency
	 */
	public void addDep(Task task) {
		taskDependecies.addDep(task);
	}

	/**
	 * Add tasks form un-serialization
	 */
	public void addUnserialized(Task task) {
		if (restoredTasks == null) restoredTasks = new ArrayList<Task>();
		restoredTasks.add(task);
	}

	/**
	 * Create a checkpoint file
	 */
	public String checkpoint(BigDataScriptNode node) {
		String programFile = programUnit.getFileName();
		String nodeFile = node.getFileName();

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
		if (checkpointFileName == null) checkpointFileName = programUnit.getFileName() + ".chp";

		if (config.isVerbose()) System.err.println("Creating checkpoint file: '" + checkpointFileName + "'");
		BigDataScriptSerializer bdsSer = new BigDataScriptSerializer(checkpointFileName, config);
		bdsSer.save(this);

		return checkpointFileName;
	}

	/**
	 * Found a node (CHECKPOINT_RECOVER run state)
	 */
	public void checkpointRecoverFound() {
		checkPointRecoverNodeIdx++;
	}

	/**
	 * Is there a next node to find? (CHECKPOINT_RECOVER run state0
	 */
	public boolean checkpointRecoverHasNextNode() {
		return pc.size() > checkPointRecoverNodeIdx;
	}

	/**
	 * Get next node to find (CHECKPOINT_RECOVER run state0
	 */
	public int checkpointRecoverNextNode() {
		return pc.nodeId(checkPointRecoverNodeIdx);
	}

	/**
	 * Create a dir for all log files
	 */
	public void createLogDir() {
		String dirname = bigDataScriptThreadId;
		File logdir = new File(dirname);
		if (!logdir.exists()) logdir.mkdirs();

		// No logging? Delete on exit
		if ((config != null) && !config.isLog()) logdir.deleteOnExit();
	}

	/**
	 * Create an HTML report (after execution finished)
	 */
	public void createReport() {
		if (getTasks().isEmpty()) {
			if (isVerbose()) Timer.showStdErr("No tasks run: Report file not created (nothing to report).");
			return;
		}

		String outFile = bigDataScriptThreadId + ".report.html";
		if (isVerbose()) Timer.showStdErr("Writing report file '" + outFile + "'");

		SimpleDateFormat csvFormat = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss");
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// Create a template
		RTemplate rTemplate = new RTemplate(BigDataScript.class, REPORT_TEMPLATE, outFile);

		//---
		// Add summary table values
		//---
		rTemplate.add("fileName", programUnit.getFileName());
		rTemplate.add("progName", Gpr.baseName(programUnit.getFileName()));
		rTemplate.add("threadId", bigDataScriptThreadId);
		rTemplate.add("runTime", (timer != null ? timer.toString() : ""));
		rTemplate.add("startTime", (timer != null ? outFormat.format(timer.getStart()) : ""));

		// Exit code
		rTemplate.add("exitValue", "" + exitValue);
		if (exitValue > 0) rTemplate.add("exitColor", REPORT_RED_COLOR);
		else rTemplate.add("exitColor", "");

		//---
		// Add task details and time-line
		//---
		int taskNum = 1;
		for (Task task : getTasks()) {
			rTemplate.add("taskNum", "" + taskNum);
			rTemplate.add("taskId", task.getId());
			rTemplate.add("taskPid", task.getPid());
			rTemplate.add("taskName", task.getName());
			rTemplate.add("taskOk", "" + task.isDoneOk());
			rTemplate.add("taskExitCode", "" + task.getExitValue());
			rTemplate.add("taskState", "" + task.getTaskState());
			rTemplate.add("taskDepState", "" + task.dependencyState());

			if (task.getFailCount() > 1) rTemplate.add("taskRetry", "" + (task.getFailCount() - 1) + "/" + (task.getMaxFailCount() - 1));
			else rTemplate.add("taskRetry", "");

			if (!task.isDoneOk()) {
				rTemplate.add("taskColor", REPORT_RED_COLOR);

				String ch = task.checkOutputFiles();
				if ((ch != null) && !ch.isEmpty()) rTemplate.add("taskCheckOut", "\n" + LINE + "Check output files" + LINE + "\n" + ch + "\n");
				else rTemplate.add("taskCheckOut", "");

				if (task.getPostMortemInfo() != null && !task.getPostMortemInfo().isEmpty()) rTemplate.add("taskPostMortemInfo", "\n" + LINE + "Post mortem info" + LINE + "\n" + task.getPostMortemInfo() + "\n");
				else rTemplate.add("taskPostMortemInfo", "");

				String tailErr = TailFile.tail(task.getStderrFile());
				if ((tailErr != null) && !tailErr.isEmpty()) rTemplate.add("taskStderr", "\n" + LINE + "Stderr" + LINE + "\n" + tailErr + "\n");
				else rTemplate.add("taskStderr", "");

				String tailOut = TailFile.tail(task.getStdoutFile());
				if ((tailOut != null) && !tailOut.isEmpty()) rTemplate.add("taskStdout", "\n" + LINE + "Stdout" + LINE + "\n" + tailOut + "\n");
				else rTemplate.add("taskStdout", "");

				if (task.getErrorMsg() != null) rTemplate.add("taskErrMsg", "\n" + LINE + "Error message" + LINE + "\n" + task.getErrorMsg() + "\n");
				else rTemplate.add("taskErrMsg", "");

			} else {
				rTemplate.add("taskColor", "");
				rTemplate.add("taskCheckOut", "");
				rTemplate.add("taskPostMortemInfo", "");
				rTemplate.add("taskStderr", "");
				rTemplate.add("taskStdout", "");
				rTemplate.add("taskErrMsg", "");
			}

			Date start = task.getRunningStartTime();
			if (start != null) {
				rTemplate.add("taskStart", outFormat.format(start));
				rTemplate.add("taskStartCsv", csvFormat.format(start));
			} else {
				rTemplate.add("taskStart", "");
				rTemplate.add("taskStartCsv", "");
			}

			Date end = task.getRunningEndTime();
			if (end != null) {
				rTemplate.add("taskEnd", outFormat.format(end));
				rTemplate.add("taskEndCsv", csvFormat.format(end));
			} else {
				rTemplate.add("taskEnd", "");
				rTemplate.add("taskEndCsv", "");
			}

			if (start != null && end != null) rTemplate.add("taskElapsed", Timer.toDDHHMMSS(end.getTime() - start.getTime()));
			else rTemplate.add("taskElapsed", "");

			// Program & hint
			rTemplate.add("taskProgram", task.getProgramTxt());
			rTemplate.add("taskHint", task.getProgramHint());

			// Dependencies
			StringBuilder sbdep = new StringBuilder();
			if (task.getDependency() != null) {
				for (Task t : task.getDependency())
					sbdep.append(t.getName() + "\n");
			}
			rTemplate.add("taskDep", sbdep.toString());

			// Input files
			StringBuilder sbinf = new StringBuilder();
			if (task.getInputFiles() != null) {
				for (String inFile : task.getInputFiles())
					sbinf.append(inFile + "\n");
			}
			rTemplate.add("taskInFiles", sbinf.toString());

			// Output files
			StringBuilder sboutf = new StringBuilder();
			if (task.getOutputFiles() != null) {
				for (String outf : task.getOutputFiles())
					sboutf.append(outf + "\n");
			}
			rTemplate.add("taskOutFiles", sboutf.toString());

			// Resources
			if (task.getResources() != null) {
				HostResources hr = task.getResources();
				rTemplate.add("taskResources", hr.toStringMultiline());
				rTemplate.add("taskTimeout", Timer.toDDHHMMSS(hr.getTimeout() * 1000));
				rTemplate.add("taskWallTimeout", Timer.toDDHHMMSS(hr.getWallTimeout() * 1000));
				rTemplate.add("taskCpus", (hr.getCpus() > 0 ? hr.getCpus() : ""));
				rTemplate.add("taskMem", (hr.getMem() > 0 ? Gpr.toStringMem(hr.getMem()) : ""));
			} else {
				rTemplate.add("taskResources", "");
				rTemplate.add("taskTimeout", "");
				rTemplate.add("taskWallTimeout", "");
				rTemplate.add("taskCpus", "");
				rTemplate.add("taskMem", "");
			}

			taskNum++;
		}

		// Number of tasks executed
		rTemplate.add("taskCount", taskNum - 1);

		// Timeline height
		int timelineHeight = REPORT_TIMELINE_HEIGHT * (1 + taskNum);
		rTemplate.add("timelineHeight", timelineHeight);

		//---
		// Show Scope
		//---
		rTemplate.add("scope.VAR_ARGS_LIST", getScope().getSymbol(Scope.GLOBAL_VAR_ARGS_LIST).toString());
		rTemplate.add("scope.TASK_OPTION_SYSTEM", getScope().getSymbol(ExpressionTask.TASK_OPTION_SYSTEM).toString());
		rTemplate.add("scope.TASK_OPTION_CPUS", getScope().getSymbol(ExpressionTask.TASK_OPTION_CPUS).toString());

		// Scope symbols
		ArrayList<ScopeSymbol> ssyms = new ArrayList<ScopeSymbol>();
		ssyms.addAll(getScope().getSymbols());
		Collections.sort(ssyms);

		if (!ssyms.isEmpty()) {

			for (ScopeSymbol ss : ssyms)
				if (!ss.getType().isFunction()) {
					rTemplate.add("symType", ss.getType());
					rTemplate.add("symName", ss.getName());
					rTemplate.add("symValue", ss.getValue());
				}
		} else {
			rTemplate.add("symType", "");
			rTemplate.add("symName", "");
			rTemplate.add("symValue", "");
		}

		//---
		// Create output file
		//---
		rTemplate.createOuptut();
	}

	/**
	 * Show a fatal error
	 */
	public void fatalError(BigDataScriptNode bdsnode, String message) {
		runState = RunState.FATAL_ERROR;
		System.err.println("Fatal error: " + bdsnode.getFileName() + ", line " + bdsnode.getLineNum() + ", pos " + bdsnode.getCharPosInLine() + ". " + message);

		// Show BDS stack trace
		System.err.println(stackTrace());

		// Create checkpoint
		String checkpointFileName = checkpoint(bdsnode);
		System.err.println("Creating checkpoint file '" + checkpointFileName + "'");

		// Set exit value
		setExitValue(1L);
	}

	/**
	 * Show a fatal error
	 */
	public void fatalError(BigDataScriptNode bdsnode, Throwable t) {
		if (runState == RunState.FATAL_ERROR) return;
		fatalError(bdsnode, t.getMessage());

		// Show java stack trace
		if ((config == null) || config.isVerbose()) t.printStackTrace();
	}

	public String getBigDataScriptThreadId() {
		return bigDataScriptThreadId;
	}

	/**
	 * Get variable's value as a bool
	 */
	public boolean getBool(String varName) {
		return (Boolean) getScope().getSymbol(varName).getValue();
	}

	public Config getConfig() {
		return config;
	}

	public int getExitValue() {
		return exitValue;
	}

	/**
	 * Get variable's value as an int
	 */
	public long getInt(String varName) {
		ScopeSymbol ssym = getScope().getSymbol(varName);
		return (Long) ssym.getValue();
	}

	/**
	 * Get variable's value as a java object
	 */
	public Object getObject(String varName) {
		return getScope().getSymbol(varName).getValue();
	}

	public ProgramCounter getPc() {
		return pc;
	}

	public BigDataScriptNode getProgramUnit() {
		return programUnit;
	}

	public Random getRandom() {
		return random;
	}

	/**
	 * Get variable's value as a real
	 */
	public double getReal(String varName) {
		return (Double) getScope().getSymbol(varName).getValue();
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public RunState getRunState() {
		return runState;
	}

	public Scope getScope() {
		return scope;
	}

	/**
	 * Get variable's value as a string
	 */
	public String getString(String varName) {
		return getScope().getSymbol(varName).getValue().toString();
	}

	/**
	 * Get a task
	 */
	public Task getTask(String taskId) {
		return tasksById.get(taskId);
	}

	/**
	 * Get all tasks
	 */
	public Collection<Task> getTasks() {
		return tasks;
	}

	/**
	 * Execute dependency tasks to achieve goal 'out'
	 */
	public synchronized List<String> goal(String out) {
		List<String> taskIds = new ArrayList<String>();

		// Find dependencies
		List<Task> tasks = taskDependecies.goal(out);

		// No update needed?
		if (tasks == null) {
			if (isDebug()) Gpr.debug("Goal '" + out + "' has no dependent tasks. Nothing to do.");
			return taskIds;
		}

		// Run all tasks
		if (isDebug()) Gpr.debug("Goal '" + out + "', dependent tasks:");
		for (Task t : tasks) {
			if (isDebug()) System.err.println("\t\t" + t.getId());
			ExpressionTask.execute(this, t);
		}

		// Convert to task IDs
		for (Task t : tasks)
			taskIds.add(t.getId());

		return taskIds;
	}

	/**
	 * Are we in CHECKPOINT_RECOVER mode?
	 */
	public boolean isCheckpointRecover() {
		return runState == RunState.CHECKPOINT_RECOVER;
	}

	public boolean isDebug() {
		return config != null && config.isDebug();
	}

	/**
	 * Have all tasks finished executing?
	 */
	public boolean isTasksDone() {
		for (String taskId : tasksById.keySet()) {
			if ((taskId == null) || taskId.isEmpty()) continue;

			Task task = getTask(taskId);
			if (task == null) continue;

			if (!task.isDone()) return false;
		}

		return true;

	}

	public boolean isVerbose() {
		return config != null && config.isVerbose();
	}

	/**
	 * Kill one task
	 */
	public void killTask(String taskId) {
		// Just send a kill to all Executioners
		for (Executioner executioner : Executioners.getInstance().getAll())
			executioner.kill(taskId);
	}

	/**
	 * Kill all tasks in the list
	 */
	@SuppressWarnings("rawtypes")
	public void killTasks(List taskIds) {
		// We are done when ALL tasks are done
		for (Object tid : taskIds)
			killTask(tid.toString());
	}

	/**
	 * Create a new scope
	 */
	public void newScope(BigDataScriptNode node) {
		scope = new Scope(scope, node);
	}

	/**
	 * Back to old scope
	 */
	public void oldScope() {
		scope = scope.getParent();
	}

	void prinntCode(String code) {
		// Show file contents
		int lineNum = 1;
		for (String line : code.split("\n"))
			System.out.println(String.format("%6d |%s", lineNum++, line));
		System.out.println("");
	}

	public void print() {
		// Create a list with program file and all included files
		List<BigDataScriptNode> nodeWithFiles = programUnit.findNodes(StatementInclude.class, true);
		nodeWithFiles.add(0, programUnit);

		// Show code
		for (BigDataScriptNode bwf : nodeWithFiles) {
			System.out.println("Program file: '" + bwf.getFileName() + "'");
			prinntCode(((BlockWithFile) bwf).getFileText());
		}

		// Show stack trace
		System.out.println("Stack trace:");
		System.out.println(stackTrace());

		// Show scopes
		for (Scope scope = getScope(); scope != null; scope = scope.getParent()) {
			if (!scope.isEmpty()) {
				BigDataScriptNode node = scope.getNode();

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

	/**
	 * Send task from un-serialization to execution list
	 */
	public void restoreUnserializedTasks() {
		if (restoredTasks == null) return;
		for (Task task : restoredTasks)
			if (!task.isDone() || (task.isFailed() && !task.isCanFail())) {
				// Task not finished or failed? Re-execute
				ExpressionTask.execute(this, task);
			} else {
				// Task finished? Just add it
				add(task);
			}

		restoredTasks = null; // We don't need it any more (plus we want to make sure we don't schedule tasks more than once)
	}

	@SuppressWarnings("rawtypes")
	public void rmOnExit(List files) {
		for (Object o : files)
			removeOnExit.add(o.toString());
	}

	public void rmOnExit(String file) {
		removeOnExit.add(file);
	}

	@Override
	public void run() {
		timer = new Timer();

		createLogDir(); // Create log dir

		// Run program
		RunState runState = null;
		try {
			runState = programUnit.run(this);
		} catch (Throwable t) {
			runState = RunState.FATAL_ERROR;
			if (isVerbose()) throw new RuntimeException(t);
			else Timer.showStdErr("Fatal error: Program execution finished");
			return;
		}

		if (isVerbose()) Timer.showStdErr("Program execution finished (run state: '" + runState + "')");

		// Implicit 'wait' statement at the end of the program
		boolean ok = waitTasksAll();

		// All tasks in wait finished OK?
		if (!ok) {
			// Errors? Then set exit status appropriately
			exitValue = 1;
		} else {
			// Set exit value as the latest 'int' result
			Object ev = getReturnValue();
			if (ev instanceof Long) exitValue = (int) ((long) ((Long) ev)); // Yes, it's a very weird cast....

			// Remove all pending files
			if (!removeOnExit.isEmpty()) {
				if (config != null && config.isNoRmOnExit()) {
					if (isVerbose()) Timer.showStdErr("\tDeleting stale files: Cancelled ('noRmOnExit' is active).");
				} else {
					if (isVerbose()) Timer.showStdErr("Deleting stale files:");
					for (String fileName : removeOnExit) {
						if (isVerbose()) System.err.println("\t" + fileName);
						(new File(fileName)).delete();
					}
				}
			}
		}

		timer.end();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		bigDataScriptThreadNum = (int) serializer.getNextFieldInt();
		removeOnExit = serializer.getNextFieldList(TypeList.get(Type.STRING));

		// Update global thread number?
		if (threadNumber < bigDataScriptThreadNum) threadNumber = bigDataScriptThreadNum + 1;
	}

	@Override
	public String serializeSave(BigDataScriptSerializer serializer) {
		StringBuilder out = new StringBuilder();

		out.append(getClass().getSimpleName());
		out.append("\t" + bigDataScriptThreadNum);
		out.append("\t" + serializer.serializeSaveValue(removeOnExit));
		out.append("\n");

		// Save program counter
		out.append(pc.serializeSave(serializer));

		// Save scopes
		out.append(scope.serializeSave(serializer));

		// Save program nodes
		out.append(programUnit.serializeSave(serializer));

		// Save all tasks (in the same order that they were added)
		for (Task task : tasks)
			out.append(task.serializeSave(serializer));

		return out.toString();
	}

	public void setExitValue(long exitValue) {
		this.exitValue = (int) exitValue;
	}

	public void setPc(ProgramCounter pc) {
		this.pc = pc;
	}

	/**
	 * Ser program unit and update bigDataScriptThreadId"
	 * @param programUnit
	 */
	public void setProgram(ProgramUnit programUnit) {
		this.programUnit = programUnit;

		// Create ID
		String name = Gpr.baseName(programUnit.getFileName());
		bigDataScriptThreadId = String.format("%s.%2$tY%2$tm%2$td_%2$tH%2$tM%2$tS_%2$tL", name, Calendar.getInstance());
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	public void setRunState(RunState runState) {
		this.runState = runState;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	/**
	 * Should we run this node?
	 * @param node
	 * @return
	 */
	public boolean shouldRun(BigDataScriptNode node) {
		// Should we run?
		switch (runState) {
		case OK:
		case BREAK:
		case CONTINUE:
		case RETURN:
			return true;

		case EXIT:
		case FATAL_ERROR:
			return false;

		case WAIT_RECOVER:
		case CHECKPOINT_RECOVER:
			break;

		default:
			throw new RuntimeException("Unhandled RunState: " + runState);
		}

		// Which node are we looking for?
		int nodeNum = checkpointRecoverNextNode();
		if (node.getId() == nodeNum) {
			// Node found!
			checkpointRecoverFound();

			// More nodes to recurse? => continue
			if (checkpointRecoverHasNextNode()) return true;

			// Last node found!
			runState = RunState.OK; // Switch to 'normal' run state
			if (node instanceof Wait) {
				runState = RunState.WAIT_RECOVER; // We want to recover all tasks that failed in wait statement
			}

			if (node instanceof Checkpoint) return false; // We want to recover AFTER the checkpoint
			return true;
		}

		return false;
	}

	void sleep() {
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show BDS calling stack
	 * @return
	 */
	public String stackTrace() {

		// Get all nodes and hash them by ID
		List<BigDataScriptNode> nodes = programUnit.findNodes(null, true);
		HashMap<Integer, BigDataScriptNode> nodesById = new HashMap<Integer, BigDataScriptNode>();
		for (BigDataScriptNode node : nodes) {
			nodesById.put(node.getId(), node);
		}
		nodesById.put(programUnit.getId(), programUnit);

		// Collect source code
		HashMap<String, String[]> fileName2codeLines = new HashMap<String, String[]>();
		for (BigDataScriptNode node : nodesById.values()) {
			if (node instanceof BlockWithFile) {
				BlockWithFile bwf = (BlockWithFile) node;
				String fileName = node.getFileName();
				String code = bwf.getFileText();
				if (fileName != null && code != null) fileName2codeLines.put(fileName, code.split("\n"));
			}
		}

		// Show stack
		StringBuilder sb = new StringBuilder();
		String linePrev = "";
		for (int nodeId : pc) {
			BigDataScriptNode node = nodesById.get(nodeId);
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
		sb.append("BigDataScriptThread: " + bigDataScriptThreadNum + "\n");
		sb.append("\tPC        : " + pc + "\n");
		sb.append("\tRun state : " + runState + "\n");
		sb.append("\tScope     : " + scope + "\n");
		sb.append("\tProgram   :\n" + programUnit.toStringTree("\t\t", "program") + "\n");
		return sb.toString();
	}

	/**
	 * Wait for one task to finish
	 * @return true if task finished OK or it was allowed to fail (i.e. canFail = true)
	 */
	public boolean waitTask(String taskId) {
		if ((taskId == null) || taskId.isEmpty()) return true;

		Task task = getTask(taskId);
		if (task == null) return false; // No task? We are done!

		if (config.isVerbose()) Timer.showStdErr("Waiting for task to finish: " + task.getId());

		// Wait for task to finish
		while (!task.isDone())
			sleep();

		// Either finished OK or it was allowed to fail
		boolean ok = task.isDoneOk() || task.isCanFail();

		// If task failed, show task information and failure reason.
		if (!ok) {
			// Show error and mark all files to be deleted on exit
			System.err.println("Task failed:\n" + task.toString(true));
			task.deleteOutputFilesOnExit();
		}

		if (config.isVerbose()) Timer.showStdErr("Task finished: " + task.getId());
		return ok;
	}

	/**
	 * Wait for all tasks in the list to finish
	 * @return true if all tasks finished OK or it were allowed to fail (i.e. canFail = true)
	 */
	@SuppressWarnings("rawtypes")
	public boolean waitTasks(List taskIds) {
		boolean ok = true;

		// We are done when ALL tasks are done
		for (Object tid : taskIds)
			ok &= waitTask(tid.toString());

		return ok;
	}

	/**
	 * Wait for all tasks to finish
	 * @return true if all tasks finished OK or it were allowed to fail (i.e. canFail = true)
	 */
	public boolean waitTasksAll() {
		// Wait for all tasks to finish
		boolean ok = true;

		if (config.isVerbose() && !isTasksDone()) Timer.showStdErr("Waiting for all tasks to finish.");
		for (String tid : tasksById.keySet())
			ok &= waitTask(tid);

		return ok;
	}

}
