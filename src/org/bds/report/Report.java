package org.bds.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bds.Bds;
import org.bds.BdsLog;
import org.bds.Config;
import org.bds.cluster.host.Resources;
import org.bds.data.Data;
import org.bds.lang.statement.Statement;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;
import org.bds.run.BdsThreads;
import org.bds.scope.GlobalScope;
import org.bds.scope.Scope;
import org.bds.task.TailFile;
import org.bds.task.Task;
import org.bds.task.TaskDependecies;
import org.bds.util.Gpr;
import org.bds.util.GprString;
import org.bds.util.Timer;

/**
 * Progress and final report (HTML & YAML)
 *
 * @author pcingola
 */
public class Report implements BdsLog {

	public static String DAG_TEMPLATE = "DagTaskTemplate.js";
	public static final String DATE_FORMAT_CSV = "yyyy,MM,dd,HH,mm,ss";
	public static final String DATE_FORMAT_HTML = "yyyy-MM-dd HH:mm:ss";
	public static final String LINE = "--------------------";
	public static final int MAX_TASK_FAILED_NAMES = 10; // Maximum number of failed tasks to show in summary
	public static final String REPORT_GREEN_COLOR = "style=\"background-color: #a2dc74\"";
	public static final String REPORT_RED_COLOR = "style=\"background-color: #ff9696\"";
	public static String REPORT_TEMPLATE = "SummaryTemplate.html";
	public static String REPORT_TEMPLATE_YAML = "SummaryTemplate.yaml";
	public static final int REPORT_TIME = 60; // Update report every 'REPORT_TIME' seconds

	public static final int REPORT_TIMELINE_HEIGHT = 42; // Size of time-line element (life, universe and everything)
	public static final String REPORT_YELLOW_COLOR = "style=\"background-color: #fdff96\"";

	protected static Timer timerReport = new Timer(); // Report timer (added by Jin Lee)

	BdsThread bdsThread;

	boolean debug;
	Map<String, BdsThread> taskId2BdsThread;
	boolean verbose;
	boolean yaml;

	/**
	 * Check if this is a good time to create a report
	 */
	public static void reportTime() {
		boolean doReport = false;

		synchronized (timerReport) {
			if (timerReport.elapsedSecs() > REPORT_TIME) { // Generate report every 'REPORT_TIME' seconds
				timerReport.start();
				doReport = true;
			}
		}

		if (doReport) {
			// Create an HTML report
			// Note: Should we create a YAML report as well?
			Report report = new Report(BdsThreads.getInstance().get().getRoot(), false);
			report.createReport();
		}
	}

	public Report(BdsThread bdsThread, boolean yaml) {
		if (!bdsThread.isRoot()) throw new RuntimeException("Cannot create report from non-root bdsThread");

		this.bdsThread = bdsThread;
		this.yaml = yaml;
		verbose = Config.get().isVerbose();
		debug = Config.get().isDebug();
	}

	/**
	 * Create an HTML report (after execution finished)
	 */
	public void createReport() {
		debug("CreateReport: Start");

		String bdsThreadId = bdsThread.getBdsThreadId();

		if (!bdsThread.anyTask()) {
			log("No tasks run: Report file not created for '" + bdsThreadId + "'.");
			return;
		}

		// Get report base name or create one
		String reportBaseName = Config.get().getReportFileName();
		if (reportBaseName == null) reportBaseName = bdsThread.getBdsThreadId();

		// Create report file names
		String outFile = reportBaseName + ".report." + (yaml ? "yaml" : "html");
		bdsThread.setReportFile(outFile);
		String dagJsFile = reportBaseName + ".dag.js";
		log("Writing report file '" + outFile + "'");

		SimpleDateFormat outFormat = new SimpleDateFormat(DATE_FORMAT_HTML);

		// Create a template
		RTemplate rTemplate = new RTemplate(Bds.class, (yaml ? REPORT_TEMPLATE_YAML : REPORT_TEMPLATE), outFile);

		//---
		// Add summary table values
		//---
		Statement statement = bdsThread.getStatement();
		Timer timer = bdsThread.getTimer();
		rTemplate.add("fileName", statement.getFileName());
		rTemplate.add("progName", Gpr.baseName(statement.getFileName()));
		rTemplate.add("threadIdRoot", bdsThreadId);
		rTemplate.add("runTime", (timer != null ? timer.toString() : ""));
		rTemplate.add("startTime", (timer != null ? outFormat.format(timer.getStart()) : ""));
		rTemplate.add("dagJsFile", dagJsFile);

		// Exit code
		int exitValue = bdsThread.getExitValue();
		rTemplate.add("exitValue", "" + exitValue);
		if (exitValue > 0) rTemplate.add("exitColor", REPORT_RED_COLOR);
		else rTemplate.add("exitColor", "");

		// Populate task to bdsThread map
		taskId2BdsThread = new HashMap<>();
		taskId2BdsThread(bdsThread);

		// Threads details
		createReport(rTemplate, bdsThread);

		// Add task details and time-line
		int taskNum = 1;
		TaskDependecies taskDepsRoot = TaskDependecies.get();
		for (Task task : taskDepsRoot.getTasks()) {
			createReport(rTemplate, task, taskNum++, yaml);
		}

		// Number of tasks executed
		rTemplate.add("taskCount", taskDepsRoot.size());
		rTemplate.add("taskFailed", taskDepsRoot.countTaskFailed());
		rTemplate.add("taskFailedNames", taskDepsRoot.taskFailedNames(MAX_TASK_FAILED_NAMES, "\n"));

		// Timeline height
		int timelineHeight = REPORT_TIMELINE_HEIGHT * (1 + taskNum);
		rTemplate.add("timelineHeight", timelineHeight);

		//---
		// Show Scope
		//---
		Scope scope = bdsThread.getScope();
		rTemplate.add("scope.VAR_ARGS_LIST", scope.getValue(GlobalScope.GLOBAL_VAR_ARGS_LIST));
		rTemplate.add("scope.TASK_OPTION_SYSTEM", scope.getValue(GlobalScope.GLOBAL_VAR_TASK_OPTION_SYSTEM));
		rTemplate.add("scope.TASK_OPTION_CPUS", scope.getValue(GlobalScope.GLOBAL_VAR_TASK_OPTION_CPUS));

		// Scope symbols
		ArrayList<String> names = new ArrayList<>();
		names.addAll(scope.getNames());
		Collections.sort(names);

		if (!names.isEmpty()) {
			for (String name : names) {
				Value val = scope.getValue(name);
				if (val != null) {
					rTemplate.add("symType", val.getType());
					rTemplate.add("symName", name);
					rTemplate.add("symValue", GprString.escape(val.toString()));
				}
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

		debug("CreateReport: End");
	}

	/**
	 * Add thread information to report
	 */
	void createReport(RTemplate rTemplate, BdsThread bdsThread) {
		debug("CreateReport BdsThreadId '" + bdsThread.getBdsThreadId() + "': Start");

		// ID and parent
		String thisId = bdsThread.getBdsThreadId();
		String thisIdNum = threadIdNum(bdsThread);

		BdsThread bdsThreadParent = bdsThread.getParent();
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
		for (BdsThread bdsThreadChild : bdsThread.getBdsThreads())
			createReport(rTemplate, bdsThreadChild);

		debug("CreateReport BdsThreadId '" + bdsThread.getBdsThreadId() + "': End");
	}

	/**
	 * Create map with task details
	 */
	void createReport(RTemplate rTemplate, Task task, int taskNum, boolean yaml) {
		debug("CreateReport Task '" + task.getId() + "': Start");

		BdsThread bdsTh = taskId2BdsThread.get(task.getId());
		SimpleDateFormat outFormat = new SimpleDateFormat(DATE_FORMAT_HTML);

		rTemplate.add("taskNum", "" + taskNum);
		rTemplate.add("taskId", Gpr.baseName(task.getId()));
		rTemplate.add("taskName", task.getName());
		rTemplate.add("taskThreadId", (bdsTh != null ? bdsTh.getBdsThreadId() : ""));
		rTemplate.add("taskThreadNum", threadIdNum(bdsTh));
		rTemplate.add("taskPid", task.getPid());
		rTemplate.add("taskOk", "" + task.isDoneOk());
		rTemplate.add("taskExitCode", "" + task.getExitValue());
		rTemplate.add("taskState", "" + task.getTaskState());
		rTemplate.add("taskDepState", "" + task.dependencyState());

		if (task.getFailCount() > 1) rTemplate.add("taskRetry", "" + (task.getFailCount() - 1) + "/" + (task.getMaxFailCount() - 1));
		else rTemplate.add("taskRetry", "");

		// Task status
		if (!task.isDoneOk()) {
			rTemplate.add("taskColor", taskColor(task));

			String ch = task.checkOutputFiles();
			if ((ch != null) && !ch.isEmpty()) rTemplate.add("taskCheckOut", multilineString("Check output files", ch, yaml));
			else rTemplate.add("taskCheckOut", "");

			if (task.getPostMortemInfo() != null && !task.getPostMortemInfo().isEmpty()) rTemplate.add("taskPostMortemInfo", multilineString("Post mortem info", task.getPostMortemInfo(), yaml));
			else rTemplate.add("taskPostMortemInfo", "");

			if (task.getErrorMsg() != null) rTemplate.add("taskErrMsg", multilineString("Error message", task.getErrorMsg(), yaml));
			else rTemplate.add("taskErrMsg", "");
		} else {
			rTemplate.add("taskColor", "");
			rTemplate.add("taskCheckOut", "");
			rTemplate.add("taskPostMortemInfo", "");
			rTemplate.add("taskErrMsg", "");
		}

		// Always show task's STDOUT/STDERR
		String tailErr = TailFile.tail(task.getStderrFile(), Config.get().getTailLines());
		if ((tailErr != null) && !tailErr.isEmpty()) rTemplate.add("taskStderr", multilineString("Stderr", tailErr, yaml));
		else rTemplate.add("taskStderr", "");

		String tailOut = TailFile.tail(task.getStdoutFile(), Config.get().getTailLines());
		if ((tailOut != null) && !tailOut.isEmpty()) rTemplate.add("taskStdout", multilineString("Stdout", tailOut, yaml));
		else rTemplate.add("taskStdout", "");

		// Running times
		Date start = task.getRunningStartTime();
		if (start != null) {
			rTemplate.add("taskStart", outFormat.format(start));
			rTemplate.add("taskStartCsv", csvDate(start));
		} else {
			rTemplate.add("taskStart", "");
			rTemplate.add("taskStartCsv", "");
		}

		Date end = task.getRunningEndTime();
		if (end == null) end = start;
		if (end != null) {
			rTemplate.add("taskEnd", outFormat.format(end));
			rTemplate.add("taskEndCsv", csvDate(end));
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
		if (task.getInputs() != null) {
			for (Data inFile : task.getInputs())
				sbinf.append(inFile + "\n");
		}
		rTemplate.add("taskInFiles", multilineString(null, sbinf.toString(), yaml));

		// Output files
		StringBuilder sboutf = new StringBuilder();
		if (task.getOutputs() != null) {
			for (Data outf : task.getOutputs())
				sboutf.append(outf + "\n");
		}
		rTemplate.add("taskOutFiles", multilineString(null, sboutf.toString(), yaml));

		// Task resources
		if (task.getResources() != null) {
			Resources hr = task.getResources();
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
		rTemplate.add("taskGraphThreadNum", threadIdNum(bdsTh));
		if (task.getDependencies() != null) {
			for (Task t : task.getDependencies()) {
				String taskName = task.getName();
				String parenTaskName = t.getName();
				rTemplate.add("taskGraphEdgeName", parenTaskName + "-" + taskName);
				rTemplate.add("taskGraphEdgeSource", parenTaskName);
				rTemplate.add("taskGraphEdgeTarget", taskName);
			}
		}

		debug("CreateReport Task '" + task.getId() + "': End");
	}

	/**
	 * Create a DAG showing all tasks
	 */
	void createTaskDag(String dagJsFile) {
		debug("Creating DAG summary script '" + dagJsFile + "'");

		// Create a template
		RTemplate rTemplate = new RTemplate(Bds.class, DAG_TEMPLATE, dagJsFile);

		// Add thread information
		createReport(rTemplate, bdsThread);

		// Add task details for DAG
		// Note: We need to add tasks again since we are creating another 'report' (the DAG file)
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
	 * Create a comma separated string representing a date (Summary report renders it via JavaScript)
	 * Code contributed by Jin Lee (to fix a bug due to stupid javascript 0-based month concept)
	 */
	String csvDate(Date date) {
		SimpleDateFormat csvFormat = new SimpleDateFormat(DATE_FORMAT_CSV);

		String strTaskStartCsv = (date != null) ? csvFormat.format(date) : csvFormat.format(new Date());
		String[] csv = strTaskStartCsv.split(",");
		csv[1] = Integer.toString(Integer.parseInt(csv[1]) - 1);
		String str = csv[0] + "," + csv[1] + "," + csv[2] + "," + csv[3] + "," + csv[4] + "," + csv[5];
		return str;
	}

	/**
	 * Force a report timer to execute a report
	 * Note: Only used in test cases
	 */
	public void forceReportTimer() {
		Date start = new Date();
		start.setTime(start.getTime() - 2 * 1000 * REPORT_TIME);
		timerReport.setStart(start);
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

	@Override
	public boolean isVerbose() {
		return verbose;
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

	String taskColor(Task task) {
		if (task.isDoneOk()) return "";
		if (task.isStateRunning()) return REPORT_GREEN_COLOR;
		if (task.getTaskState().isStartFailed()) return REPORT_YELLOW_COLOR;
		if (task.isStateError()) return REPORT_RED_COLOR;
		return "";
	}

	void taskId2BdsThread(BdsThread bdsThread) {
		// Add all tasks in this thread
		for (Task t : bdsThread.getTasks())
			taskId2BdsThread.put(t.getId(), bdsThread);

		// Recurse to child threads
		for (BdsThread bdsThreadChild : bdsThread.getBdsThreads())
			taskId2BdsThread(bdsThreadChild);
	}

	String threadIdNum(BdsThread bdsThread) {
		if (bdsThread == null) return "None";
		if (bdsThread.getParent() == null) return "thread_Root";
		return "thread_" + bdsThread.getId();
	}

}
