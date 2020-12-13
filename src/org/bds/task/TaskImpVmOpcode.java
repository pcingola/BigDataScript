package org.bds.task;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Execute a 'taskimp' VM opcode for an improper task
 *
 * The opcode requires having the following items in the stack
 *   1) Command to execute
 *   2) Input dependencies
 *   3) Output dependencies
 *   4) Checkpoint (local) file path
 *
 * @author pcingola
 */
public class TaskImpVmOpcode extends TaskVmOpcode {

	protected String checkpointLocalFile;

	public TaskImpVmOpcode(BdsThread bdsThread, boolean usePid) {
		super(bdsThread, usePid);
	}

	protected Task createTask(String sysCmds, String checkpointFile) {
		Task task = createTask(sysCmds);
		task.setImproper(true);
		task.setCheckpointLocalFile(checkpointFile);
		return task;
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
		Value chpFile = bdsThread.pop();

		taskDependency = createTaskDependency(outs, ins);

		// Create task
		String checkpointFile = chpFile.asString();
		task = createTask(cmd.asString(), checkpointFile);

		// Schedule task for execution
		dispatchTask(task);

		// Push taskId to stack
		return task.getId();
	}

	@Override
	protected String taskId() {
		return id("taskimp");
	}

}
