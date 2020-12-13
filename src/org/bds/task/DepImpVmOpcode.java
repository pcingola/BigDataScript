package org.bds.task;

import org.bds.run.BdsThread;

/**
 * Execute a 'dep' VM opcode using an improper task
 *
 * @author pcingola
 */
public class DepImpVmOpcode extends TaskImpVmOpcode {

	public DepImpVmOpcode(BdsThread bdsThread, boolean usePid) {
		super(bdsThread, usePid);
	}

	/**
	 * Dispatch task for execution
	 */
	@Override
	protected void dispatchTask(Task task) {
		task.setDependency(true); // Mark this as a 'dependency'
		bdsThread.add(task);
	}

	@Override
	protected String taskId() {
		return id("depimp");
	}

}
