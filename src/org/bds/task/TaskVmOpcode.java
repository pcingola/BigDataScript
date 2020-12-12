package org.bds.task;

import java.util.HashMap;

import org.bds.cluster.host.TaskResources;
import org.bds.data.Data;
import org.bds.data.DataRemote;
import org.bds.executioner.Executioner;
import org.bds.executioner.Executioners;
import org.bds.executioner.Executioners.ExecutionerType;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.ExpressionTask;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;
import org.bds.scope.GlobalScope;

/**
 * Execute a 'task' VM opcode
 *
 * The opcode requires having the following items in the stack
 *   1) Command to execute
 *   2) Input dependencies
 *   3) Output dependencies
 *
 * @author pcingola
 */
public class TaskVmOpcode extends SysVmOpcode {

	protected Task task;
	protected TaskDependency taskDependency;

	/**
	 * Execute a task (schedule it into executioner)
	 */
	public static void execute(BdsThread bdsThread, Task task) {
		// Select executioner and queue for execution
		String runSystem = bdsThread.getString(GlobalScope.GLOBAL_VAR_TASK_OPTION_SYSTEM);
		Executioner executioner = Executioners.getInstance().get(runSystem, bdsThread);
		task.execute(bdsThread, executioner); // Execute task
	}

	public TaskVmOpcode(BdsThread bdsThread, boolean usePid) {
		super(bdsThread, usePid);
	}

	/**
	 * Create commands that will be executed in a shell
	 */
	protected String createCommands(String sysCmds) {
		HashMap<String, String> replace = new HashMap<>();
		StringBuilder sbDown = new StringBuilder();
		StringBuilder sbUp = new StringBuilder();

		if (taskDependency != null) {
			//---
			// Are there any remote inputs?
			// We need to create the appropriate 'download' commands
			//---
			if (taskDependency.getInputs() != null) {
				for (Data dataIn : taskDependency.getInputs()) {
					if (dataIn.isRemote()) {
						String uriStr = dataIn.getUrlOri();
						sbDown.append(ExpressionTask.CMD_DOWNLOAD //
								+ " \"" + dataIn.url() + "\"" //
								+ " \"" + dataIn.getLocalPath() + "\"" //
								+ "\n");

						replace.put(uriStr, dataIn.getLocalPath());
					}
				}
			}

			//---
			// Are there any remote outputs?
			// We need to create the appropriate 'upload' commands
			//---
			if (taskDependency.getOutputs() != null) {
				for (Data dataOut : taskDependency.getOutputs()) {
					if (dataOut.isRemote()) {
						String uriStr = dataOut.getUrlOri();
						sbUp.append(ExpressionTask.CMD_UPLOAD //
								+ " \"" + dataOut.getLocalPath() + "\"" //
								+ " \"" + dataOut.url() + "\"" //
								+ "\n");

						replace.put(uriStr, dataOut.getLocalPath());

						// Note, commands executed locally will output to the local file, so
						// we must make sure that the path exists (otherwise the command
						// results in an error.
						((DataRemote) dataOut).mkdirsLocal();
					}
				}
			}
		}

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

	/**
	 * Create a task
	 */
	protected Task createTask(String sysCmds) {
		// Get an ID
		String taskId = taskId();

		// Get commands representing a shell program
		sysCmds = createCommands(sysCmds);

		// Create Task
		String sysFileName = getSysFileName(taskId);
		Task task = new Task(taskId, getBdsNode(), sysFileName, sysCmds);

		// Configure Task parameters
		task.setVerbose(bdsThread.getConfig().isVerbose());
		task.setDebug(bdsThread.getConfig().isDebug());

		// Set task options
		task.setTaskName(getTaskName());
		task.setAllowEmpty(bdsThread.getBool(GlobalScope.GLOBAL_VAR_TASK_OPTION_ALLOW_EMPTY));
		task.setCanFail(bdsThread.getBool(GlobalScope.GLOBAL_VAR_TASK_OPTION_CAN_FAIL));
		task.setCurrentDir(bdsThread.getCurrentDir());
		task.setNode(bdsThread.getString(GlobalScope.GLOBAL_VAR_TASK_OPTION_NODE));
		task.setMaxFailCount((int) bdsThread.getInt(GlobalScope.GLOBAL_VAR_TASK_OPTION_RETRY) + 1); // Note: Max fail count is the number of retries plus one (we always run at least once)
		task.setDetached(bdsThread.getBool(GlobalScope.GLOBAL_VAR_TASK_OPTION_DETACHED));

		// Set task options: Resources
		String runSystem = bdsThread.getString(GlobalScope.GLOBAL_VAR_TASK_OPTION_SYSTEM);
		ExecutionerType exType = ExecutionerType.parseSafe(runSystem);
		TaskResources res = TaskResources.factory(exType);
		res.setFromBdsThread(bdsThread);
		task.setResources(res);

		if (taskDependency != null) task.setTaskDependency(taskDependency);

		return task;
	}

	protected TaskDependency createTaskDependency(ValueList outs, ValueList ins) {
		TaskDependency td = null;

		if (ins.isEmpty() && outs.isEmpty()) return null;
		td = new TaskDependency(getBdsNode());

		for (Value in : ins)
			td.addInput(in.asString());

		for (Value out : outs)
			td.addOutput(out.asString());

		return td;
	}

	/**
	 * Dispatch task for execution
	 */
	protected void dispatchTask(Task task) {
		execute(bdsThread, task);
	}

	@Override
	protected String getTaskName() {
		return bdsThread.hasVariable(GlobalScope.GLOBAL_VAR_TASK_OPTION_TASKNAME) ? bdsThread.getString(GlobalScope.GLOBAL_VAR_TASK_OPTION_TASKNAME) : null;
	}

	@Override
	protected boolean isNode(BdsNode n) {
		return n instanceof ExpressionTask;
	}

	/**
	 * Replace all occurrences in 'replace' map
	 */
	protected String replace(HashMap<String, String> replace, String sysCmds) {
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
	protected String replace(String oldStr, String newStr, String str) {
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
	 * Create and run task
	 */
	@Override
	public String run() {
		// Create task: Get values from stack
		Value cmd = bdsThread.pop();
		ValueList ins = (ValueList) bdsThread.pop();
		ValueList outs = (ValueList) bdsThread.pop();

		taskDependency = createTaskDependency(outs, ins);

		// Create task
		task = createTask(cmd.asString());

		// Schedule task for execution
		dispatchTask(task);

		// Push taskId to stack
		return task.getId();
	}

	protected String taskId() {
		return id("task");
	}

}
