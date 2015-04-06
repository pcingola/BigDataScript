package ca.mcgill.mcb.pcingola.bigDataScript.run;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
import ca.mcgill.mcb.pcingola.bigDataScript.lang.FunctionCall;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.MethodCall;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.ProgramUnit;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Statement;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.StatementInclude;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Wait;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Exec;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerialize;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.task.TailFile;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.task.TaskDependecies;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.GprString;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * A threads used in a bigDataScript program
 *
 * It has all information to run a program (scope, pc, run state, etc)
 *
 * @author pcingola
 */
public class BigDataScriptThread extends Thread implements BigDataScriptSerialize {

	public static final String DATE_FORMAT_CSV = "yyyy,MM,dd,HH,mm,ss";
	public static final String DATE_FORMAT_HTML = "yyyy-MM-dd HH:mm:ss";
	public static String REPORT_TEMPLATE = "SummaryTemplate.html";
	public static String REPORT_TEMPLATE_YAML = "SummaryTemplate.yaml";
	public static String DAG_TEMPLATE = "DagTaskTemplate.js";
	public static final String REPORT_RED_COLOR = "style=\"background-color: #ffc0c0\"";
	public static final int REPORT_TIMELINE_HEIGHT = 42; // Size of time-line element (life, universe and everything)
	public static final String LINE = "--------------------";

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
	Object returnValue; // Latest return value (from a 'return' statement)
	int exitValue; // Exit value
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
	Deque<Object> stack;

	// BdsThread
	String currentDir; // Program's 'current directoy'
	BigDataScriptThread parent; // Parent thread
	String bdsThreadId; // BdsThread ID
	int bdsThreadNum; // Thread number
	Map<String, BigDataScriptThread> bdsChildThreadsById; // Child threads

	// Task management
	TaskDependecies taskDependecies;
	List<Task> restoredTasks; // Unserialized tasks.

	/**
	 * Get an ID for a node
	 */
	protected synchronized static int bigDataScriptThreadId() {
		return threadNumber++;
	}

	public BigDataScriptThread(Statement statement, BigDataScriptThread parent) {
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

		bdsChildThreadsById = new HashMap<String, BigDataScriptThread>();
		taskDependecies = new TaskDependecies();

		setStatement(statement);
		parent.add(this);

		taskDependecies.setVerbose(isVerbose());
		taskDependecies.setDebug(isDebug());
	}

	public BigDataScriptThread(Statement statement, Config config) {
		super();
		bdsThreadNum = bigDataScriptThreadId();
		pc = new ProgramCounter();
		scope = Scope.getGlobalScope();
		stack = new LinkedList<>();
		runState = RunState.OK;
		this.config = config;
		random = new Random();
		removeOnExit = new LinkedList<String>();
		taskDependecies = new TaskDependecies();
		bdsChildThreadsById = new HashMap<String, BigDataScriptThread>();
		currentDir = System.getProperty(Exec.USER_DIR); // By default use Java program's current dir

		if (statement != null) setStatement(statement);

		taskDependecies.setVerbose(isVerbose());
		taskDependecies.setDebug(isDebug());
	}

	/**
	 * Add a child task
	 */
	public synchronized void add(BigDataScriptThread bdsThread) {
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
		if (restoredTasks == null) restoredTasks = new ArrayList<Task>();
		restoredTasks.add(task);
	}

	/**
	 * Has this or any child thread created tasks?
	 */
	boolean anyTask() {
		if (!getTasks().isEmpty()) return true;

		for (BigDataScriptThread bdsThread : bdsChildThreadsById.values())
			if (bdsThread.anyTask()) return true;

		return false;
	}

	/**
	 * Create a checkpoint file
	 */
	public String checkpoint(BigDataScriptNode node) {
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
		BigDataScriptSerializer bdsSer = new BigDataScriptSerializer(checkpointFileName, config);
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
	 * Create an HTML report (after execution finished)
	 */
	public void createReport(boolean yaml) {
		if (!anyTask()) {
			if (isVerbose()) Timer.showStdErr("No tasks run: Report file not created for '" + getBdsThreadId() + "'.");
			return;
		}

		String outFile = getBdsThreadId() + ".report." + (yaml ? "yaml" : "html");
		String dagJsFile = getBdsThreadId() + ".dag.js";
		if (isVerbose()) Timer.showStdErr("Writing report file '" + outFile + "'");

		SimpleDateFormat outFormat = new SimpleDateFormat(DATE_FORMAT_HTML);

		// Create a template
		RTemplate rTemplate = new RTemplate(BigDataScript.class, (yaml ? REPORT_TEMPLATE_YAML : REPORT_TEMPLATE), outFile);

		//---
		// Add summary table values
		//---
		rTemplate.add("fileName", statement.getFileName());
		rTemplate.add("progName", Gpr.baseName(statement.getFileName()));
		rTemplate.add("threadIdRoot", bdsThreadId);
		rTemplate.add("runTime", (timer != null ? timer.toString() : ""));
		rTemplate.add("startTime", (timer != null ? outFormat.format(timer.getStart()) : ""));
		rTemplate.add("dagJsFile", dagJsFile);

		// Exit code
		rTemplate.add("exitValue", "" + exitValue);
		if (exitValue > 0) rTemplate.add("exitColor", REPORT_RED_COLOR);
		else rTemplate.add("exitColor", "");

		// Threads details
		createReport(rTemplate, this, null);

		// Add task details and time-line
		int taskNum = 1;
		for (Task task : TaskDependecies.get().getTasks())
			createReport(rTemplate, task, taskNum++, yaml);

		// Number of tasks executed
		rTemplate.add("taskCount", taskNum - 1);
		rTemplate.add("taskFailed", taskDependecies.countTaskFailed());
		rTemplate.add("taskFailedNames", taskDependecies.taskFailedNames(MAX_TASK_FAILED_NAMES, "\n"));

		// Timeline height
		int timelineHeight = REPORT_TIMELINE_HEIGHT * (1 + taskNum);
		rTemplate.add("timelineHeight", timelineHeight);

		//---
		// Show Scope
		//---
		rTemplate.add("scope.VAR_ARGS_LIST", getScope().getSymbol(Scope.GLOBAL_VAR_ARGS_LIST).getValue());
		rTemplate.add("scope.TASK_OPTION_SYSTEM", getScope().getSymbol(ExpressionTask.TASK_OPTION_SYSTEM).getValue());
		rTemplate.add("scope.TASK_OPTION_CPUS", getScope().getSymbol(ExpressionTask.TASK_OPTION_CPUS).getValue());

		// Scope symbols
		ArrayList<ScopeSymbol> ssyms = new ArrayList<ScopeSymbol>();
		ssyms.addAll(getScope().getSymbols());
		Collections.sort(ssyms);

		if (!ssyms.isEmpty()) {
			for (ScopeSymbol ss : ssyms)
				if (!ss.getType().isFunction()) {
					rTemplate.add("symType", ss.getType());
					rTemplate.add("symName", ss.getName());
					rTemplate.add("symValue", GprString.escape(ss.getValue().toString()));
				}
		} else {
			rTemplate.add("symType", "");
			rTemplate.add("symName", "");
			rTemplate.add("symValue", "");
		}

		// Create output file
		rTemplate.createOuptut();

		// Create DAG script
		if (!yaml) createTaskDag(dagJsFile);
	}

	/**
	 * Add thread information to report
	 */
	void createReport(RTemplate rTemplate, BigDataScriptThread bdsThread, BigDataScriptThread bdsThreadParent) {
		// ID and parent
		String thisId = bdsThread.getBdsThreadId();
		String thisIdNum = threadIdNum(bdsThread);
		String parenId = (bdsThreadParent != null ? bdsThreadParent.getBdsThreadId() : "Null");
		String parenIdNum = threadIdNum(bdsThreadParent);
		rTemplate.add("threadId", thisId);
		rTemplate.add("threadIdNum", thisIdNum);
		rTemplate.add("threadParent", parenId);
		rTemplate.add("threadParentNum", parenIdNum);

		//---
		// Graph
		//---
		rTemplate.add("threadGraphIdNum", thisIdNum);
		rTemplate.add("threadGraphEdgeId", parenIdNum + "-" + thisIdNum);
		rTemplate.add("threadGraphEdgeSource", parenIdNum);
		rTemplate.add("threadGraphEdgeTarget", thisIdNum);

		//---
		// Add tasks
		//---
		StringBuilder sb = new StringBuilder();
		for (Task t : bdsThread.getTasks())
			sb.append(t.getId() + "\n");
		rTemplate.add("threadTasks", sb.toString());

		// Recurse to child threads
		for (BigDataScriptThread bdsThreadChild : bdsThread.bdsChildThreadsById.values())
			createReport(rTemplate, bdsThreadChild, bdsThread);
	}

	/**
	 * Create map with task details
	 */
	void createReport(RTemplate rTemplate, Task task, int taskNum, boolean yaml) {
		BigDataScriptThread bdsThread = findBdsThread(task);
		SimpleDateFormat csvFormat = new SimpleDateFormat(DATE_FORMAT_CSV);
		SimpleDateFormat outFormat = new SimpleDateFormat(DATE_FORMAT_HTML);

		rTemplate.add("taskNum", "" + taskNum);
		rTemplate.add("taskId", Gpr.baseName(task.getId()));
		rTemplate.add("taskName", task.getName());
		rTemplate.add("taskThreadId", (bdsThread != null ? bdsThread.getBdsThreadId() : ""));
		rTemplate.add("taskThreadNum", threadIdNum(bdsThread));
		rTemplate.add("taskPid", task.getPid());
		rTemplate.add("taskOk", "" + task.isDoneOk());
		rTemplate.add("taskExitCode", "" + task.getExitValue());
		rTemplate.add("taskState", "" + task.getTaskState());
		rTemplate.add("taskDepState", "" + task.dependencyState());

		if (task.getFailCount() > 1) rTemplate.add("taskRetry", "" + (task.getFailCount() - 1) + "/" + (task.getMaxFailCount() - 1));
		else rTemplate.add("taskRetry", "");

		// Task status
		if (!task.isDoneOk()) {
			rTemplate.add("taskColor", REPORT_RED_COLOR);

			String ch = task.checkOutputFiles();
			if ((ch != null) && !ch.isEmpty()) rTemplate.add("taskCheckOut", multilineString("Check output files", ch, yaml));
			else rTemplate.add("taskCheckOut", "");

			if (task.getPostMortemInfo() != null && !task.getPostMortemInfo().isEmpty()) rTemplate.add("taskPostMortemInfo", multilineString("Post mortem info", task.getPostMortemInfo(), yaml));
			else rTemplate.add("taskPostMortemInfo", "");

			String tailErr = TailFile.tail(task.getStderrFile());
			if ((tailErr != null) && !tailErr.isEmpty()) rTemplate.add("taskStderr", multilineString("Stderr", tailErr, yaml));
			else rTemplate.add("taskStderr", "");

			String tailOut = TailFile.tail(task.getStdoutFile());
			if ((tailOut != null) && !tailOut.isEmpty()) rTemplate.add("taskStdout", multilineString("Stdout", tailOut, yaml));
			else rTemplate.add("taskStdout", "");

			if (task.getErrorMsg() != null) rTemplate.add("taskErrMsg", multilineString("Error message", task.getErrorMsg(), yaml));
			else rTemplate.add("taskErrMsg", "");

		} else {
			rTemplate.add("taskColor", "");
			rTemplate.add("taskCheckOut", "");
			rTemplate.add("taskPostMortemInfo", "");
			rTemplate.add("taskStderr", "");
			rTemplate.add("taskStdout", "");
			rTemplate.add("taskErrMsg", "");
		}

		// Running times
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
		rTemplate.add("taskProgram", multilineString(null, task.getProgramTxt(), yaml));
		rTemplate.add("taskHint", task.getProgramHint());

		// Dependencies
		StringBuilder sbdep = new StringBuilder();
		if (task.getDependencies() != null) {
			for (Task t : task.getDependencies()) {
				sbdep.append(t.getName() + "\n");
			}
		}
		rTemplate.add("taskDep", multilineString(null, sbdep.toString(), yaml));

		// Input files
		StringBuilder sbinf = new StringBuilder();
		if (task.getInputFiles() != null) {
			for (String inFile : task.getInputFiles())
				sbinf.append(inFile + "\n");
		}
		rTemplate.add("taskInFiles", multilineString(null, sbinf.toString(), yaml));

		// Output files
		StringBuilder sboutf = new StringBuilder();
		if (task.getOutputFiles() != null) {
			for (String outf : task.getOutputFiles())
				sboutf.append(outf + "\n");
		}
		rTemplate.add("taskOutFiles", multilineString(null, sboutf.toString(), yaml));

		// Task resources
		if (task.getResources() != null) {
			HostResources hr = task.getResources();
			rTemplate.add("taskResources", multilineString(null, hr.toStringMultiline(), yaml));
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

		//---
		// Graph plot
		//---
		rTemplate.add("taskGraphName", task.getName());
		rTemplate.add("taskGraphThreadNum", threadIdNum(bdsThread));
		if (task.getDependencies() != null) {
			for (Task t : task.getDependencies()) {
				String taskName = task.getName();
				String parenTaskName = t.getName();
				rTemplate.add("taskGraphEdgeName", parenTaskName + "-" + taskName);
				rTemplate.add("taskGraphEdgeSource", parenTaskName);
				rTemplate.add("taskGraphEdgeTarget", taskName);
			}
		}
	}

	/**
	 * Create a DAG showing all tasks
	 */
	void createTaskDag(String dagJsFile) {
		if (isDebug()) Timer.showStdErr("Creating DAG summary script '" + dagJsFile + "'");

		// Create a template
		RTemplate rTemplate = new RTemplate(BigDataScript.class, DAG_TEMPLATE, dagJsFile);

		// Add thread information
		createReport(rTemplate, this, null);

		// Add task details for DAG
		int taskNum = 1;
		for (Task task : TaskDependecies.get().getTasks())
			createReport(rTemplate, task, taskNum++, false);

		// Add at least one fake edge, so rTemplate doesn't fail
		rTemplate.add("threadGraphEdgeId", "threadid-threadid");
		rTemplate.add("threadGraphEdgeSource", "threadid");
		rTemplate.add("threadGraphEdgeTarget", "threadid");

		rTemplate.add("taskGraphEdgeName", "taskid-taskid");
		rTemplate.add("taskGraphEdgeSource", "taskid");
		rTemplate.add("taskGraphEdgeTarget", "taskid");

		// Create output file
		rTemplate.createOuptut();
	}

	/**
	 * Running in debug mode: This method is invoked right before running 'node'
	 */
	void debug(BigDataScriptNode node) {
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
	void debugStep(BigDataScriptNode node) {
		// Show current line
		String prg = node.toString();
		if (prg.indexOf("\n") > 0) prg = "\n" + Gpr.prependEachLine("\t", prg);
		else prg = prg + " ";

		String prompt = "DEBUG [" + debugMode + "]: " //
				+ node.getFileName() //
				+ ", line " + node.getLineNum() //
				+ (isVerbose() ? " (" + node.getClass().getSimpleName() + ")" : "") //
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
	void debugUpdatePc(BigDataScriptNode node) {
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
	public void fatalError(BigDataScriptNode bdsnode, String message) {
		runState = RunState.FATAL_ERROR;
		String filePos = "";
		if (bdsnode.getFileNameCanonical() != null) filePos = bdsnode.getFileName() + ", line " + bdsnode.getLineNum() + ", pos " + (bdsnode.getCharPosInLine() + 1) + ". ";
		System.err.println("Fatal error: " + filePos + message);

		// Show BDS stack trace
		try {
			System.err.println(stackTrace());
		} catch (Throwable t) {
			t.printStackTrace();
		}

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
		if ((config == null) || isVerbose()) t.printStackTrace();
	}

	/**
	 * Find bdsThread that has 'task'
	 */
	public BigDataScriptThread findBdsThread(Task task) {
		if (getTask(task.getId()) != null) return this;

		for (BigDataScriptThread childBdsTh : bdsChildThreadsById.values()) {
			BigDataScriptThread bdsThFound = childBdsTh.findBdsThread(task);
			if (bdsThFound != null) return bdsThFound;
		}

		return null;

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

	public List<BigDataScriptThread> getBdsThreads() {
		List<BigDataScriptThread> list = new ArrayList<BigDataScriptThread>();
		list.addAll(bdsChildThreadsById.values());
		return list;
	}

	/**
	 * Get all child threads
	 */
	public List<BigDataScriptThread> getBdsThreadsAll() {
		List<BigDataScriptThread> list = new ArrayList<BigDataScriptThread>();
		list.add(this);

		for (BigDataScriptThread bth : bdsChildThreadsById.values())
			list.addAll(bth.getBdsThreadsAll());

		return list;
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
	 * Get variable's value as an int
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
	 * Get variable's value as a java object
	 */
	public Object getObject(String varName) {
		return getScope().getSymbol(varName).getValue();
	}

	public BigDataScriptThread getParent() {
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
	 * Get variable's value as a real
	 */
	public double getReal(String varName) {
		return (Double) getScope().getSymbol(varName).getValue();
	}

	public Object getReturnValue() {
		return returnValue;
	}

	/**
	 * Get 'root' thread
	 */
	public BigDataScriptThread getRoot() {
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

	public Statement getStatement() {
		return statement;
	}

	public String getStatementNodeId() {
		return statementNodeId;
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
		return taskDependecies.getTask(taskId);
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
	public synchronized BigDataScriptThread getThread(String threadId) {
		// Do we have this thread?
		BigDataScriptThread bdsth = bdsChildThreadsById.get(threadId);
		if (bdsth != null) return bdsth;

		// Search in all child threads
		for (BigDataScriptThread child : bdsChildThreadsById.values()) {
			bdsth = child.getThread(threadId);
			if (bdsth != null) return bdsth;
		}

		// Not found
		return null;
	}

	/**
	 * Execute dependency tasks to achieve goal 'out'
	 */
	public synchronized List<String> goal(String out) {
		List<String> taskIds = new ArrayList<String>();

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

	/**
	 * Is it in RunState exit mode?
	 */
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
	 * Convert multi-line string for report
	 */
	String multilineString(String title, String str, boolean yaml) {
		if (yaml) {
			// Convert to YAML multi-line
			return Gpr.prependEachLine("        ", str).trim();
		}

		// Use lines and title separators
		if (title != null) return "\n" + LINE + title + LINE + "\n" + str + "\n";

		// Nothing to do for regular HTML lines
		return str;
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

	public Object peek() {
		if (isCheckpointRecover()) return null;
		return stack.peek();
	}

	public Object pop() {
		if (isCheckpointRecover()) return null;
		return stack.removeFirst();
	}

	public void print() {
		// Create a list with program file and all included files
		List<BigDataScriptNode> nodeWithFiles = statement.findNodes(StatementInclude.class, true);
		nodeWithFiles.add(0, statement);

		// Show code
		for (BigDataScriptNode bwf : nodeWithFiles) {
			System.out.println("Program file: '" + bwf.getFileName() + "'");
			printCode(((BlockWithFile) bwf).getFileText());
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

	void printCode(String code) {
		// Show file contents
		int lineNum = 1;
		for (String line : code.split("\n"))
			System.out.println(String.format("%6d |%s", lineNum++, line));
		System.out.println("");
	}

	public void push(Object obj) {
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
	public void remove(BigDataScriptThread bdsThread) {
		bdsChildThreadsById.remove(bdsThread);
	}

	/**
	 * Remove stale files (controlled by '-noRmOnExit' command line option)
	 */
	void removeStaleFiles() {
		if (!isRoot()) return;

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
			removeOnExit.add(o.toString());
	}

	public synchronized void rmOnExit(String file) {
		removeOnExit.add(file);
	}

	@Override
	public void run() {
		timer = new Timer();

		createLogDir(); // Create log dir

		// Start child threads (e.g. when recovering)
		for (BigDataScriptThread bth : bdsChildThreadsById.values()) {
			if (!bth.isAlive() && !bth.isFinished()) bth.start();
		}

		// Add this thread to collections
		BigDataScriptThreads.getInstance().add(this);

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
			if (isVerbose()) Timer.showStdErr((isRoot() ? "Program" : "Parallel") + " '" + getBdsThreadId() + "' execution finished");

			// Implicit 'wait' statement at the end of the program
			ok = waitAll();
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
		removeStaleFiles();
		timer.end();
		if (config != null && isRoot()) {
			// Create reports? Note that some people may want both HTML and YAML reports
			if (config.isReportHtml()) createReport(false);
			if (config.isReportYaml()) createReport(true);
		}
		if (!isRoot()) parent.remove(this); // Remove from parent's threads

		// OK, we are done
		if (isVerbose()) {
			// Root thread? Report all tasks
			TaskDependecies td = isRoot() ? TaskDependecies.get() : taskDependecies;

			Timer.showStdErr((isRoot() ? "Program" : "Parallel") + " " //
					+ "'" + getBdsThreadId() + "'" //
					+ " finished" //
					+ (isDebug() ? ", run state: '" + runState + "'" : "") //
					+ ", exit value: " + getExitValue() //
					+ ", tasks executed: " + td.getTasks().size() //
					+ ", tasks failed: " + td.countTaskFailed() //
					+ ", tasks failed names: " + td.taskFailedNames(MAX_TASK_FAILED_NAMES, " , ") //
					+ "." //
			);
		}

		// Remove thread from map
		BigDataScriptThreads.getInstance().remove(this);
	}

	/**
	 * Run this node
	 */
	public void run(BigDataScriptNode node) {
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
	protected void runBegin(BigDataScriptNode node) {
		// Need a new scope?
		if (node.isNeedsScope()) newScope(node);
		getPc().push(node);

	}

	/**
	 * Run after running the node
	 */
	protected void runEnd(BigDataScriptNode node) {
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
	public void serializeParse(BigDataScriptSerializer serializer) {
		bdsThreadNum = (int) serializer.getNextFieldInt();
		removeOnExit = serializer.getNextFieldList(TypeList.get(Type.STRING));
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

		// Stack
		String b64 = serializer.getNextField();
		stack = (b64 != null && !b64.isEmpty() ? (Deque<Object>) serializer.base64Decode(b64) : null);
	}

	@Override
	public String serializeSave(BigDataScriptSerializer serializer) {
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
	public String serializeSaveAll(BigDataScriptSerializer serializer) {
		StringBuilder out = new StringBuilder();
		out.append(serializeSaveThreadMain(serializer));
		out.append("\n");
		out.append(serializeSaveThreadData(serializer));
		return out.toString();
	}

	/**
	 * Save thread's data
	 */
	protected String serializeSaveThreadData(BigDataScriptSerializer serializer) {
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
		for (BigDataScriptThread bdsTh : bdsChildThreadsById.values())
			out.append(serializer.serializeSave(bdsTh));

		return out.toString();
	}

	/**
	 * Save thread's main information
	 */
	protected String serializeSaveThreadMain(BigDataScriptSerializer serializer) {
		StringBuilder out = new StringBuilder();

		out.append(getClass().getSimpleName());
		out.append("\t" + bdsThreadNum);
		out.append("\t" + serializer.serializeSaveValue(removeOnExit));
		out.append("\t" + serializer.serializeSaveValue(getBdsThreadId()));
		out.append("\t" + serializer.serializeSaveValue(statement.getNodeId()));
		out.append("\t" + serializer.serializeSaveValue(scope.getNodeId()));
		out.append("\t" + serializer.serializeSaveValue(parent != null ? parent.getBdsThreadId() : ""));
		out.append("\t" + serializer.serializeSaveValue(runState.toString()));
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
	 * Find and set statement
	 */
	public void setStatement(Map<String, BigDataScriptSerialize> nodesById) {
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
			if (node instanceof Checkpoint) return false; // We want to recover AFTER the checkpoint
			return true;
		}

		return false;
	}

	/**
	 * Show BDS calling stack
	 */
	public String stackTrace() {

		// Get all nodes and hash them by ID
		List<BigDataScriptNode> nodes = statement.findNodes(null, true);
		HashMap<Integer, BigDataScriptNode> nodesById = new HashMap<Integer, BigDataScriptNode>();
		for (BigDataScriptNode node : nodes) {
			nodesById.put(node.getId(), node);
		}
		nodesById.put(statement.getId(), statement);

		// Collect source code
		HashMap<String, String[]> fileName2codeLines = new HashMap<String, String[]>();
		for (BigDataScriptNode node : nodesById.values()) {
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

	String threadIdNum(BigDataScriptThread bdsThread) {
		if (bdsThread == null) return "None";
		if (bdsThread.getParent() == null) return "thread_Root";
		return "thread_" + bdsThread.getId();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("BigDataScriptThread: " + bdsThreadNum + "\n");
		sb.append("\tPC        : " + pc + "\n");
		sb.append("\tRun state : " + runState + "\n");
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
		BigDataScriptThread bdsThRoot = getRoot();
		BigDataScriptThread bdsTh = bdsThRoot.getThread(id);
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
	public boolean waitThread(BigDataScriptThread bdsThread) {

		try {
			if (bdsThread != null) {
				if (isVerbose()) Timer.showStdErr("Waiting for parallel '" + bdsThread.getBdsThreadId() + "' to finish. RunState: " + bdsThread.getRunState());
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

		if (isVerbose() && !isThreadsDone()) Timer.showStdErr("Waiting for all 'parrallel' to finish.");
		for (BigDataScriptThread bdsth : bdsChildThreadsById.values())
			ok &= waitThread(bdsth);

		return ok;
	}

}
