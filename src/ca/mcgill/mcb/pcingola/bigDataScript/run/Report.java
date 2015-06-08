package ca.mcgill.mcb.pcingola.bigDataScript.run;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript;
import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.htmlTemplate.RTemplate;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.ExpressionTask;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Statement;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
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
public class Report {

	public static final String DATE_FORMAT_CSV = "yyyy,MM,dd,HH,mm,ss";
	public static final String DATE_FORMAT_HTML = "yyyy-MM-dd HH:mm:ss";
	public static String REPORT_TEMPLATE = "SummaryTemplate.html";
	public static String REPORT_TEMPLATE_YAML = "SummaryTemplate.yaml";
	public static String DAG_TEMPLATE = "DagTaskTemplate.js";
	public static final String REPORT_RED_COLOR = "style=\"background-color: #ffc0c0\"";
	public static final int REPORT_TIMELINE_HEIGHT = 42; // Size of time-line element (life, universe and everything)
	public static final String LINE = "--------------------";

	public static final int MAX_TASK_FAILED_NAMES = 10; // Maximum number of failed tasks to show in summary

	boolean yaml;
	boolean verbose;
	boolean debug;
	BigDataScriptThread bdsThread;

	public Report(BigDataScriptThread bdsThread, boolean yaml) {
		this.bdsThread = bdsThread;
		this.yaml = yaml;
		this.verbose = Config.get().isVerbose();
		this.debug = Config.get().isDebug();
	}

	/**
	 * Create an HTML report (after execution finished)
	 */
	public void createReport() {
		String bdsThreadId = bdsThread.getBdsThreadId();

		if (!bdsThread.anyTask()) {
			if (verbose) Timer.showStdErr("No tasks run: Report file not created for '" + bdsThreadId + "'.");
			return;
		}

		String outFile = bdsThread.getBdsThreadId() + ".report." + (yaml ? "yaml" : "html");
		String dagJsFile = bdsThread.getBdsThreadId() + ".dag.js";
		if (verbose) Timer.showStdErr("Writing report file '" + outFile + "'");

		SimpleDateFormat outFormat = new SimpleDateFormat(DATE_FORMAT_HTML);

		// Create a template
		RTemplate rTemplate = new RTemplate(BigDataScript.class, (yaml ? REPORT_TEMPLATE_YAML : REPORT_TEMPLATE), outFile);

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

		// Threads details
		createReport(rTemplate, this, null);

		// Add task details and time-line
		int taskNum = 1;
		TaskDependecies taskDepsRoot = TaskDependecies.get();
		for (Task task : taskDepsRoot.getTasks())
			createReport(rTemplate, task, taskNum++, yaml);

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
		rTemplate.add("scope.VAR_ARGS_LIST", scope.getSymbol(Scope.GLOBAL_VAR_ARGS_LIST).getValue());
		rTemplate.add("scope.TASK_OPTION_SYSTEM", scope.getSymbol(ExpressionTask.TASK_OPTION_SYSTEM).getValue());
		rTemplate.add("scope.TASK_OPTION_CPUS", scope.getSymbol(ExpressionTask.TASK_OPTION_CPUS).getValue());

		// Scope symbols
		ArrayList<ScopeSymbol> ssyms = new ArrayList<ScopeSymbol>();
		ssyms.addAll(scope.getSymbols());
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
	void createReport(RTemplate rTemplate, Report bdsThread, Report bdsThreadParent) {
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
		for (Report bdsThreadChild : bdsThread.bdsChildThreadsById.values())
			createReport(rTemplate, bdsThreadChild, bdsThread);
	}

	/**
	 * Create map with task details
	 */
	void createReport(RTemplate rTemplate, Task task, int taskNum, boolean yaml) {
		Report bdsThread = findBdsThread(task);
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

			String tailErr = TailFile.tail(task.getStderrFile(), Config.get().getTailLines());
			if ((tailErr != null) && !tailErr.isEmpty()) rTemplate.add("taskStderr", multilineString("Stderr", tailErr, yaml));
			else rTemplate.add("taskStderr", "");

			String tailOut = TailFile.tail(task.getStdoutFile(), Config.get().getTailLines());
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
			rTemplate.add("taskStartCsv", csvDate(start));
		} else {
			rTemplate.add("taskStart", "");
			rTemplate.add("taskStartCsv", "");
		}

		Date end = task.getRunningEndTime();
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

}
