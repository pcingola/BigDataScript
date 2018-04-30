package org.bds.task;

import org.bds.run.BdsThread;

/**
 * Execute a 'dep' VM opcode
 *
 * @author pcingola
 */
public class DepFactory extends TaskFactory {

	public DepFactory(BdsThread bdsThread) {
		super(bdsThread);
	}

	/**
	 * Dispatch task for execution
	 */
	@Override
	void dispatchTask(Task task) {
		task.setDependency(true); // Mark this as a 'dependency'
		bdsThread.add(task);
	}
}
