package org.bds.task;

import java.util.HashMap;

import org.bds.data.Data;
import org.bds.data.DataRemote;
import org.bds.executioner.Executioner;
import org.bds.executioner.Executioners;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.ExpressionTask;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Execute a 'task' VM opcode
 *
 * @author pcingola
 */
public class TaskFactory extends SysFactory {

	Task task;
	TaskDependency taskDependency;

	/**
	 * Execute a task (schedule it into executioner)
	 */
	public static void execute(BdsThread bdsThread, Task task) {
		// Select executioner and queue for execution
		String runSystem = bdsThread.getString(ExpressionTask.TASK_OPTION_SYSTEM);
		Executioner executioner = Executioners.getInstance().get(runSystem);
		task.execute(bdsThread, executioner); // Execute task
	}

	public TaskFactory(BdsThread bdsThread) {
		super(bdsThread);
	}

	/**
	 * Try to find the current bdsNode
	 */
	@Override
	protected BdsNode bdsNode() {
		BdsNode n = bdsThread.getBdsNodeCurrent();

		// Try to find a 'task' node
		for (BdsNode bn = n; bn != null; bn = bn.getParent()) {
			if (bn instanceof ExpressionTask) return bn;
		}

		// Not found? Use this as default
		return n;
	}

	/**
	 * Create commands that will be executed in a shell
	 */
	String createCommands(String sysCmds) {
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
						sbDown.append(ExpressionTask.CMD_DOWNLOAD //
								+ " \"" + dataIn.getUri() + "\"" //
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
						sbUp.append(ExpressionTask.CMD_UPLOAD //
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
	Task createTask(String sysCmds) {
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
		task.setCanFail(bdsThread.getBool(ExpressionTask.TASK_OPTION_CAN_FAIL));
		task.setAllowEmpty(bdsThread.getBool(ExpressionTask.TASK_OPTION_ALLOW_EMPTY));
		task.setNode(bdsThread.getString(ExpressionTask.TASK_OPTION_NODE));
		task.setQueue(bdsThread.getString(ExpressionTask.TASK_OPTION_QUEUE));
		task.setMaxFailCount((int) bdsThread.getInt(ExpressionTask.TASK_OPTION_RETRY) + 1); // Note: Max fail count is the number of retries plus one (we always run at least once)
		task.setCurrentDir(bdsThread.getCurrentDir());

		// Set task options: Resources
		task.getResources().setCpus((int) bdsThread.getInt(ExpressionTask.TASK_OPTION_CPUS));
		task.getResources().setMem(bdsThread.getInt(ExpressionTask.TASK_OPTION_MEM));
		task.getResources().setWallTimeout(bdsThread.getInt(ExpressionTask.TASK_OPTION_WALL_TIMEOUT));
		task.getResources().setTimeout(bdsThread.getInt(ExpressionTask.TASK_OPTION_TIMEOUT));
		if (taskDependency != null) task.setTaskDependency(taskDependency);

		return task;
	}

	TaskDependency createTaskDependency(ValueList outs, ValueList ins) {
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
	void dispatchTask(Task task) {
		execute(bdsThread, task);
	}

	@Override
	protected String getTaskName() {
		return bdsThread.hasVariable(ExpressionTask.TASK_OPTION_TASKNAME) ? bdsThread.getString(ExpressionTask.TASK_OPTION_TASKNAME) : null;
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
	 * Create and run task
	 */
	@Override
	public String run() {
		// Create task
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

	String taskId() {
		return sysId("task");
	}

}
