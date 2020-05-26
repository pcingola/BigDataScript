package org.bds.task;

import org.bds.executioner.Executioner;
import org.bds.executioner.Executioners;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.ExpressionTask;
import org.bds.run.BdsThread;

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
public class TaskVmOpcode extends ShellVmOpcode {

	/**
	 * Execute a task (schedule it into executioner)
	 */
	public static void execute(BdsThread bdsThread, Task task) {
		// Select executioner and queue for execution
		String runSystem = bdsThread.getString(ExpressionTask.TASK_OPTION_SYSTEM);
		Executioner executioner = Executioners.getInstance().get(runSystem);
		task.execute(bdsThread, executioner); // Execute task
	}

	public TaskVmOpcode(BdsThread bdsThread) {
		super(bdsThread);
	}

	/**
	 * Dispatch task for execution
	 */
	@Override
	protected void dispatchTask(Task task) {
		execute(bdsThread, task);
	}

	@Override
	protected boolean isNode(BdsNode n) {
		return n instanceof ExpressionTask;
	}

}
