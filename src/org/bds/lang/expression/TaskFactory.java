package org.bds.lang.expression;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bds.data.Data;
import org.bds.data.DataRemote;
import org.bds.executioner.Executioner;
import org.bds.executioner.Executioners;
import org.bds.run.BdsThread;
import org.bds.task.Task;
import org.bds.task.TaskDependency;
import org.bds.util.Gpr;

/**
 * Execute a 'task' VM opcode 
 *
 * @author pcingola
 */
public class TaskFactory {

	private static int sysId = 1;

	/**
	 * Execute a task (schedule it into executioner)
	 */
	public static void execute(BdsThread bdsThread, Task task) {
		// Select executioner and queue for execution
		String runSystem = bdsThread.getString(TASK_OPTION_SYSTEM);
		Executioner executioner = Executioners.getInstance().get(runSystem);
		task.execute(bdsThread, executioner); // Execute task
	}

	/**
	 * Get a sys ID
	 */
	private static synchronized int nextId() {
		return sysId++;
	}

	public TaskFactory() {
	}

	/**
	 * Create commands that will be executed in a shell
	 */
	String createCommands(BdsThread bdsThread, TaskDependency taskDependency, ExpressionSys x) {
		HashMap<String, String> replace = new HashMap<>();
		StringBuilder sbDown = new StringBuilder();
		StringBuilder sbUp = new StringBuilder();

		if (taskDependency != null) {
			//---
			// Are there any remote inputs?
			// We need to create the appropriate 'download' commands
			//---
			if (taskDependency.getInputs() != null) {
				for (String in : taskDependency.getInputs()) {
					Data dataIn = Data.factory(in);
					if (dataIn.isRemote()) {
						sbDown.append(CMD_DOWNLOAD //
								+ " \"" + dataIn.getAbsolutePath() + "\"" //
								+ " \"" + dataIn.getLocalPath() + "\"" //
								+ "\n");

						replace.put(dataIn.getAbsolutePath(), dataIn.getLocalPath());
					}
				}
			}

			//---
			// Are there any remote outputs?
			// We need to create the appropriate 'upload' commands
			//---
			if (taskDependency.getOutputs() != null) {
				for (String out : taskDependency.getOutputs()) {
					Data dataOut = Data.factory(out);
					if (dataOut.isRemote()) {
						sbUp.append(CMD_UPLOAD //
								+ " \"" + dataOut.getLocalPath() + "\"" //
								+ " \"" + dataOut.getAbsolutePath() + "\"" //
								+ "\n");

						replace.put(dataOut.getAbsolutePath(), dataOut.getLocalPath());

						// Note, commands executed locally will output to the local file, so
						// we must make sure that the path exists (otherwise the command
						// results in an error.
						((DataRemote) dataOut).mkdirsLocal();
					}
				}
			}
		}

		//---
		// Sys commands
		// Command from string or interpolated vars
		//---
		String sysCmds = bdsThread.pop().asString();

		// No Down/Up-load? Just return the SYS commands
		if (sbDown.length() <= 0 && sbUp.length() <= 0) return sysCmds;

		// Replace all occurrences of remote references
		sysCmds = replace(replace, sysCmds);

		// Put everything together
		StringBuilder sbSys = new StringBuilder();
		if (sbDown.length() > 0) {
			sbSys.append("# Download commands\n");
			sbSys.append(sbDown);
		}
		sbSys.append(sysCmds);
		if (sbUp.length() > 0) {
			sbSys.append("\n# Upload commands\n");
			sbSys.append(sbUp);
		}

		return sbSys.toString();
	}

	public String getSysFileName(String execId) {
		if (execId == null) throw new RuntimeException("Exec ID is null. This should never happen!");

		String sysFileName = execId + ".sh";
		File f = new File(sysFileName);
		try {
			return f.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException("cannot get cannonical path for file '" + sysFileName + "'");
		}
	}

	/**
	 * Create a task
	 */
	Task createTask(BdsThread bdsThread, TaskDependency taskDependency, String commands) {
		// Task name
		String taskName = "";
		if (bdsThread.hasVariable(TASK_OPTION_TASKNAME)) taskName = bdsThread.getString(TASK_OPTION_TASKNAME);

		// Get an ID
		String execId = taskId("task", getFileName(), taskName, bdsThread);

		// Get commands representing a shell program
		String sysCmds = createCommands(bdsThread, taskDependency, commands);

		// Create Task
		Task task = new Task(execId, this, getSysFileName(execId), sysCmds);

		// Configure Task parameters
		task.setVerbose(bdsThread.getConfig().isVerbose());
		task.setDebug(bdsThread.getConfig().isDebug());

		// Set task options
		task.setTaskName(taskName);
		task.setCanFail(bdsThread.getBool(TASK_OPTION_CAN_FAIL));
		task.setAllowEmpty(bdsThread.getBool(TASK_OPTION_ALLOW_EMPTY));
		task.setNode(bdsThread.getString(TASK_OPTION_NODE));
		task.setQueue(bdsThread.getString(TASK_OPTION_QUEUE));
		task.setMaxFailCount((int) bdsThread.getInt(TASK_OPTION_RETRY) + 1); // Note: Max fail count is the number of retries plus one (we always run at least once)
		task.setCurrentDir(bdsThread.getCurrentDir());

		// Set task options: Resources
		task.getResources().setCpus((int) bdsThread.getInt(TASK_OPTION_CPUS));
		task.getResources().setMem(bdsThread.getInt(TASK_OPTION_MEM));
		task.getResources().setWallTimeout(bdsThread.getInt(TASK_OPTION_WALL_TIMEOUT));
		task.getResources().setTimeout(bdsThread.getInt(TASK_OPTION_TIMEOUT));
		if (taskDependency != null) task.setTaskDependency(taskDependency);

		return task;
	}

	/**
	 * Dispatch task for execution
	 */
	void dispatchTask(BdsThread bdsThread, Task task) {
		execute(bdsThread, task);
	}

	/**
	 * Replace all occurrences in 'replace' map
	 */
	String replace(HashMap<String, String> replace, String sysCmds) {

		for (String key : replace.keySet()) {
			String sysCmdsPrev;
			do {
				sysCmdsPrev = sysCmds;
				sysCmds = replace(key, replace.get(key), sysCmds);
			} while (!sysCmdsPrev.equals(sysCmds)); // Continue while there are replacements
		}

		return sysCmds;
	}

	/**
	 * Replace a single instance of 'oldStr' by 'newStr'
	 */
	String replace(String oldStr, String newStr, String str) {
		int start = str.indexOf(oldStr);
		if (start < 0) return str; // Nothing found

		// Check that 'oldStr' is a separated / quoted word
		int end = start + oldStr.length();
		char prevChar = start > 0 ? str.charAt(start - 1) : '\0';
		char nextChar = end < str.length() ? str.charAt(end) : '\0';

		// Change if surrounded by spaces or quotes
		boolean change = false;
		if (prevChar == '\'' && nextChar == '\'') {
			// Surrounded by single quote
			change = true;
		} else if (prevChar == '"' && nextChar == '"') {
			// Surrounded by double quote
			change = true;
		} else if ((prevChar == ' ' || prevChar == '\t' || prevChar == '\n' || prevChar == '\0') && //
				(nextChar == ' ' || nextChar == '\t' || nextChar == '\n' || nextChar == '\0')) {
			// Surrounded by space, tab, newline or end_of_string
			change = true;
		}

		// Change oldStr by newStr?
		if (change) { //
			return str.substring(0, start) // Keep first
					+ newStr // Replace oldStr by newStr
					+ (nextChar == '\0' ? "" : str.substring(start + oldStr.length())) // Last part only if there is something after 'oldStr
			;
		}

		return str;
	}

	/**
	 * Create a task ID
	 */
	protected synchronized String taskId(String name, String fileName, String taskName, BdsThread bdsThread) {
		int nextId = nextId();

		// Use module name
		String module = fileName;
		if (module != null) module = Gpr.removeExt(Gpr.baseName(module));

		if (taskName != null) {
			if (taskName.isEmpty()) taskName = null;
			else taskName = Gpr.sanityzeName(taskName); // Make sure that 'taskName' can be used in a filename
		}

		String execId = bdsThread.getBdsThreadId() //
				+ "/" + name //
				+ (module == null ? "" : "." + module) //
				+ (taskName == null ? "" : "." + taskName) //
				+ ".line_" + getLineNum() //
				+ ".id_" + nextId //
		;

		return execId;
	}

	public static void run(BdsThread bdsThread) {
		// Evaluate task options (get a list of dependencies)
		TaskDependency taskDependency = null;

		//		if (options != null) {
		//			taskDependency = options.evalTaskDependency(bdsThread);
		//
		//			if (bdsThread.isCheckpointRecover()) return;
		//
		//			if (taskDependency == null) {
		//				// Task options clause not satisfied. Do not execute task => Return empty taskId
		//				if (bdsThread.isDebug()) log("Task dependency check (needsUpdate=false): null");
		//				bdsThread.push("");
		//				return;
		//			}
		//
		//			// Needs update?
		//			taskDependency.setDebug(bdsThread.isDebug());
		//			boolean needsUpdate = taskDependency.depOperator();
		//
		//			if (bdsThread.isDebug()) log("Task dependency check (needsUpdate=" + needsUpdate + "): " + taskDependency);
		//			if (!needsUpdate) {
		//				// Task options clause not satisfied. Do not execute task => Return empty taskId
		//				bdsThread.push("");
		//				return;
		//			}
		//		}

		// Evaluate 'sys' statements
		String commands = bdsThread.pop().asString();
		Gpr.debug("COMMANDS: " + commands);

		// Create task
		Task task = createTask(bdsThread, taskDependency, commands);

		// Schedule task for execution
		dispatchTask(bdsThread, task);

		bdsThread.push(task.getId());
	}

}
