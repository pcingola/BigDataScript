package org.bds.executioner;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.bds.run.BdsThread;
import org.bds.task.Task;
import org.bds.task.TaskState;
import org.bds.util.Gpr;
import org.bds.util.Timer;

/**
 * Monitor a task: Check if a task finished by checking if 'exitFile' exists
 *
 * @author pcingola
 */
public class MonitorTask implements Serializable {

	private static final long serialVersionUID = -5364964372060576944L;

	// Cluster scheduling is usually quite slow, so we don't need a short monitoring interval.
	// Reducing this sleep time adds processing and probably has not many benefits.
	public static final int SLEEP_TIME = 500;

	boolean debug = false;
	boolean verbose;
	HashMap<Task, Executioner> execByTask;
	Timer latestUpdate;

	public MonitorTask() {
		execByTask = new HashMap<>();
		latestUpdate = new Timer();
	}

	/**
	 * Add a task to this 'timer'
	 *
	 * @param executioner : Executioner executing this task (we must be able to kill the task)
	 * @param task : Task (timeout is inferred from task.resources)
	 */
	public synchronized void add(Executioner executioner, Task task) {
		if (debug) Timer.showStdErr("MonitorTask: Adding task " + task.getId());
		if (task == null) return;
		execByTask.put(task, executioner);
	}

	/**
	 * Run once every SLEEP_TIME
	 */
	public synchronized void check() {
		// Is it time to update?
		if (latestUpdate.elapsed() < SLEEP_TIME) return;

		updateFinished();
		latestUpdate.start();
	}

	/**
	 * Remove task (do not monitor)
	 */
	public synchronized void remove(Task task) {
		if (debug) Timer.showStdErr("MonitorTask: Removing task " + task.getId());
		execByTask.remove(task);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Update finished tasks.
	 * Check if 'exitFile' exist and update states accordingly
	 */
	synchronized void updateFinished() {
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
			if (exitFileOk && debug) Timer.showStdErr("MonitorTask.updateFinished(): Found exit file '" + exitFileName + "'");

			if (exitFileOk || task.isTimedOut()) {
				if (debug) Timer.showStdErr("MonitorTask.updateFinished(): Adding task to list of finished tasks '" + task.getId() + "'");
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

	/**
	 * Update finished task.
	 */
	synchronized void updateFinished(Task task) {
		if (debug) Timer.showStdErr("MonitorTask.updateFinished(task): Finished task " + task.getId());

		int exitCode = 0;
		TaskState taskState = null;

		if (task.isTimedOut()) {
			// Timed out
			exitCode = BdsThread.EXITCODE_TIMEOUT;
			taskState = TaskState.ERROR_TIMEOUT;
		} else {
			// Exit file found: Parse exit file
			String exitFileStr = Gpr.readFile(task.getExitCodeFile()).trim();
			exitCode = (exitFileStr.equals("0") ? 0 : 1); // Anything else than OK is error condition
			taskState = null; // Automatic: let taskFinished decide, based on 'exitCode'
			if (debug) Timer.showStdErr("MonitorTask: Task finished '" + task.getId() + "', exit status : '" + exitFileStr + "', exit code " + exitCode);
		}

		// Inform executioner that task has finished
		Executioner executioner = execByTask.get(task);
		task.setExitValue(exitCode);
		executioner.taskFinished(task, taskState);
	}
}
