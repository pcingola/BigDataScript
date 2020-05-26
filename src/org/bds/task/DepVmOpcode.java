package org.bds.task;

import org.bds.run.BdsThread;

/**
 * Execute a 'dep' VM opcode
 *
 * @author pcingola
 */
public class DepVmOpcode extends TaskVmOpcode {

	public DepVmOpcode(BdsThread bdsThread, boolean usePid) {
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
}
