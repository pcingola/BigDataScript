package org.bds.executioner;

import java.io.Serializable;
import java.util.HashMap;

import org.bds.BdsLog;
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
public abstract class MonitorTask implements Serializable, BdsLog {

	private static final long serialVersionUID = -6872022158839941923L;

	protected boolean debug;
	protected boolean verbose;
	protected HashMap<Task, Executioner> execByTask;
	protected Timer latestUpdate;

	public MonitorTask() {
		execByTask = new HashMap<>();
		latestUpdate = new Timer();
	}

	/**
	 * Add a task to this 'monitor'
	 *
	 * @param executioner : Executioner executing this task (we must be able to kill the task)
	 * @param task : Task (timeout is inferred from task.resources)
	 */
	public synchronized void add(Executioner executioner, Task task) {
		if (task == null) return;
		debug("Adding task " + task.getId());
		execByTask.put(task, executioner);
	}

	/**
	 * Check if tasks have finished
	 */
	public abstract void check();

	@Override
	public boolean isDebug() {
		return debug;
	}

	@Override
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Remove task (do not monitor)
	 */
	public synchronized void remove(Task task) {
		debug("Removing task " + task.getId());
		execByTask.remove(task);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	protected abstract void updateFinished();

	/**
	 * Update finished task.
	 */
	protected synchronized void updateFinished(Task task) {
		debug("Finished task " + task.getId());

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
			debug("Update task finished '" + task.getId() + "', exit status : '" + exitFileStr + "', exit code " + exitCode);
		}

		// Inform executioner that task has finished
		Executioner executioner = execByTask.get(task);
		task.setExitValue(exitCode);
		executioner.taskFinished(task, taskState);
	}
}
