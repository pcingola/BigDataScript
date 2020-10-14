package org.bds.executioner;

import java.io.Serializable;

import org.bds.util.Timer;

/**
 * Monitor a task: Check if a task finished by checking if 'exitFile' exists
 *
 * @author pcingola
 */
public class MonitorTaskExitFile extends MonitorTask implements Serializable {

	private static final long serialVersionUID = -5364964372060576944L;

	// Cluster scheduling is usually quite slow, so we don't need a short monitoring interval.
	// Reducing this sleep time adds processing and probably has not many benefits.
	public static final int SLEEP_TIME = 500;

	protected Timer latestUpdate;

	public MonitorTaskExitFile() {
		super();
		latestUpdate = new Timer();
	}

	/**
	 * Run once every SLEEP_TIME
	 */
	@Override
	public synchronized void check() {
		// Is it time to update?
		if (latestUpdate.elapsed() < SLEEP_TIME) return;

		updateFinished();
		latestUpdate.start();
	}

}
