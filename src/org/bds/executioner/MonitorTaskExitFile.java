package org.bds.executioner;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import org.bds.task.Task;
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

	/**
	 * Update finished tasks.
	 * Check if 'exitFile' exist and update states accordingly
	 */
	@Override
	protected synchronized void updateFinished() {
		ArrayList<Task> toUpdate = null;

		for (Task task : execByTask.keySet()) {
			String exitFileName = task.getExitCodeFile();

			// Check that 'exitFile' exists and it is not zero length
			// From 'Fedor Gusev':
			//     ...here NFS is somewhat slow, and the file is still empty
			//     and it report exit code as 1. But if I check the file manually, it
			//     has 0 in it. I've introduced a check for non-zero length of
			//     the file and the problem is gone.
			File exitFile = new File(exitFileName);
			boolean exitFileOk = exitFile.exists() && exitFile.length() > 0;
			if (exitFileOk) debug("MonitorTask.updateFinished(): Found exit file '" + exitFileName + "'");

			if (exitFileOk || task.isTimedOut()) {
				debug("Adding task to list of finished tasks '" + task.getId() + "'");
				// Create (or add) to tasks to delete
				if (toUpdate == null) toUpdate = new ArrayList<>();
				toUpdate.add(task);
			}
		}

		// An task to delete?
		if (toUpdate != null) {
			for (Task task : toUpdate) {
				updateFinished(task);
				remove(task); // We don't need to monitor this task any more
			}
		}
	}

}
