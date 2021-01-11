package org.bds.executioner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.bds.BdsLog;
import org.bds.Config;
import org.bds.run.BdsThread;
import org.bds.task.Task;
import org.bds.task.TaskState;
import org.bds.util.Timer;

/**
 * Check that tasks are still running.
 *
 * This method should query the operating system, cluster system or
 * whatever 'low level' system to make sure the tasks are still
 * running.
 *
 * The idea is that if a task is killed, removed or somehow is no
 * longer running, we should catch it here (this is the last resort).
 *
 * @author pcingola
 */
public abstract class CheckTasksRunning implements BdsLog {

	public static final int CHECK_TASK_RUNNING_INTERVAL = 60;
	public static final int TASK_STATE_MIN_START_TIME = 30; // We assume that in less then this number of seconds we might not have a task reported by the cluster system
	public static final int TASK_NOT_FOUND_DISAPPEARED = 3; // How many times do we have to 'not find' a task to consider it gone

	protected boolean debug;
	protected boolean verbose;
	protected Timer time; // Timer for checking that tasks are still running
	protected Executioner executioner;
	protected Map<String, Integer> missingCount; // How many times was a task missing?
	protected Map<String, Task> taskById;

	public CheckTasksRunning(Config config, Executioner executioner) {
		this.executioner = executioner;
		missingCount = new HashMap<>();
		taskById = new HashMap<>();

		// Set debug
		debug = config.isDebug();
		verbose = config.isVerbose();
	}

	public void add(Task task) {
		taskById.put(task.getId(), task);
	}

	/**
	 * Check that tasks are actually running
	 *
	 * Default behavior:
	 * 		i) Run a command to query running PIDs
	 * 		ii) Parse command output, extract all PIDs
	 * 		iii) For each PID, find the corresponding task, add task 'taskFoundId' (HashSet<String>)
	 * 		iv) If any task was not found, mark it as an finished/error
	 */
	public void check() {
		if (!shouldCheck()) return; // Check every now and then

		Set<Task> taskFound = findRunningTasks();

		// If any 'running' tasks was not not found, mark is as finished ('ERROR')
		tasksRunning(taskFound);
	}

	/**
	 * Find a running task given a PID
	 */
	protected Set<Task> findRunningTaskByPid(Set<String> pids) {
		HashSet<Task> tasks = new HashSet<>();

		// Find task by PID
		for (Task t : executioner.getTasksRunning()) {
			String pid = t.getPid();

			if (pid != null) {
				// Matches pid?
				if (matchesPids(pids, pid)) {
					debug("Found task PID '" + pid + "'");
					tasks.add(t);
				}
			}
		}

		return tasks;
	}

	/**
	 * Run tasks that are currently running
	 */
	protected abstract Set<Task> findRunningTasks();

	/**
	 * Increment counter that keeps track on how many times in a row a task was missing
	 * @return true if task should be considered 'missing'
	 */
	protected boolean incMissingCount(Task task) {
		String id = task.getId();
		int count = (missingCount.containsKey(id) ? missingCount.get(id) + 1 : 1);
		missingCount.put(id, count);

		warning("Task PID '" + task.getPid() + "' not found for task '" + id + "'. Incrementing 'missing counter': " + count + " (max. allowed " + TASK_NOT_FOUND_DISAPPEARED + ")");
		return count > TASK_NOT_FOUND_DISAPPEARED;
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

	@Override
	public boolean isVerbose() {
		return verbose;
	}

	protected boolean matchesPids(Set<String> pids, String pid) {
		return pids.contains(pid);
	}

	public synchronized void remove(Task task) {
		taskById.remove(task.getId());
	}

	/**
	 * Reset counter (task was found)
	 */
	protected void resetMissingCount(Task task) {
		missingCount.remove(task.getId());
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Should we query task states? (i.e. run a command to see if tasks are still alive)
	 */
	protected boolean shouldCheck() {
		if (time == null) time = new Timer();
		if (time.elapsedSecs() > CHECK_TASK_RUNNING_INTERVAL) {
			time.start(); // Restart timer
			return true;
		}
		return false;
	}

	/**
	 * Update tasks according to cluster status
	 */
	protected synchronized void tasksRunning(Set<Task> taskFoundId) {
		LinkedList<Task> finished = null;

		// Any 'running' task that was not found should be marked as finished/ERROR
		for (Task task : executioner.getTasksRunning()) {

			if (!taskFoundId.contains(task) // Task not found by command?
					&& (task.elapsedSecs() > TASK_STATE_MIN_START_TIME) // Make sure that it's been running for a while (otherwise it might that the task has just started and the cluster is not reporting it yet)
					&& !task.isDone() // Is the task "not finished"?
			) {
				// Task is missing.
				// Update counter: Should we consider this task as 'missing'?
				if (incMissingCount(task)) {
					if (finished == null) finished = new LinkedList<>();
					finished.add(task);
				}
			} else {
				// Task was found, reset 'missing' counter (if any)
				resetMissingCount(task);
			}
		}

		//---
		// Any task to mark as finished/ERROR?
		//---
		if (finished != null) {
			for (Task task : finished) {
				String tpid = task.getPid() != null ? task.getPid() : "";

				if (!tpid.isEmpty() && !task.isDone()) { // Make sure the task is not finished (race conditions?)
					debug("Task PID '" + task.getPid() + "' not found. Marking it as 'disappeared'.");
					task.setErrorMsg("Task disappeared");
					task.setExitValue(BdsThread.EXITCODE_ERROR);
					executioner.taskFinished(task, TaskState.ERROR);
				}
			}
		}
	}

}
