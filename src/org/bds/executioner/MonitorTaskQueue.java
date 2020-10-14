package org.bds.executioner;

import java.io.Serializable;

import org.bds.util.Gpr;

/**
 * Monitor a task: Check if a task finished by checking if 'exitFile' exists
 *
 * @author pcingola
 */
public class MonitorTaskQueue extends MonitorTask implements Serializable {

	private static final long serialVersionUID = 85396803332085797L;

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

}
