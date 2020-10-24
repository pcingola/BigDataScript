package org.bds.executioner;

import java.io.Serializable;

import org.bds.util.Gpr;

/**
 * Monitor a task:
 *   - Read messages from the queue
 *   - Check if a task finished
 *
 * @author pcingola
 */
public class MonitorTaskQueue extends MonitorTask implements Serializable {

	private static final long serialVersionUID = 2294558678839375573L;

	public MonitorTaskQueue() {
		super();
	}

	/**
	 * Run once every SLEEP_TIME
	 */
	@Override
	public synchronized void check() {
		// Nothing to do?
		Gpr.debug("MonitorTaskQueue: CHECK !?");
	}

	@Override
	protected void updateFinished() {
		// TODO: !!!!!!!!!!
		Gpr.debug("UNIMPLEMENTED!!!!");

	}

}
