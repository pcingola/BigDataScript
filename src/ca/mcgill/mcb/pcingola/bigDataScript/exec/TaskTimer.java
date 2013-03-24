package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.util.HashMap;
import java.util.TimerTask;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Task timer
 * 
 * @author pcingola
 */
public class TaskTimer extends TimerTask {

	public static boolean debug = false;

	boolean verbose = false;
	HashMap<Task, Timer> timerByTask;
	HashMap<Task, Executioner> execByTask;
	java.util.Timer shortestTimer;
	Task shortesTask;

	public TaskTimer() {
		timerByTask = new HashMap<Task, Timer>();
		execByTask = new HashMap<Task, Executioner>();
	}

	/**
	 * Add a task to this 'timer'
	 * 
	 * @param executioner : Executioner executing this task (we must be able to kill the task)
	 * @param task : Task (timeout is inferred from task.resources)
	 */
	public synchronized void add(Executioner executioner, Task task) {
		if (debug) Gpr.debug("TaskTimer: Adding timer for task " + task.getId());
		// Sanity check
		if (task == null) return;
		if (task.getResources() == null) return;
		if (task.getResources().getTimeout() <= 0) return;

		// Create and start timer
		long secs = task.getResources().getTimeout(); // Timer out is in seconds
		Timer timer = new Timer(secs * 1000);
		timer.start();
		timerByTask.put(task, timer);
		execByTask.put(task, executioner);

		// Calculate and activate shortest timer
		createShrotestTimer();
	}

	/**
	 * Look at all remaining timeouts and create a timer for the shortest one
	 */
	void createShrotestTimer() {
		if (shortesTask != null) shortestTimer.cancel();
		shortesTask = null;
		shortestTimer = null;

		// Find shortest timeout
		long minTimeOut = Long.MAX_VALUE;
		for (Task task : timerByTask.keySet()) {
			Timer timer = timerByTask.get(task);

			long r = timer.remaining();
			if (r < minTimeOut) {
				minTimeOut = r;
				shortesTask = task;
			}
		}

		// Create timer
		if (shortesTask != null) {
			if (debug) Gpr.debug("TaskTimer: Shortest time is " + minTimeOut + " for task " + shortesTask.getId());
			shortestTimer = new java.util.Timer();
			shortestTimer.schedule(this, minTimeOut);
		} else {
			if (debug) Gpr.debug("TaskTimer: No task to timeout found. No timer created");
		}
	}

	public synchronized void remove(Task task) {
		timerByTask.remove(task);
		execByTask.remove(task);
	}

	/**
	 * Invoked by 'shortestTimer' timeout
	 */
	@Override
	public void run() {
		if (!shortesTask.isDone()) {
			Executioner executioner = execByTask.get(shortesTask);
			if (verbose) Timer.showStdErr("Task '" + shortesTask.getId() + "' timed out. Killing it");
			executioner.kill(shortesTask);
		}

		remove(shortesTask); // This should be removed by executioner, but just in case...
		createShrotestTimer(); // Need a new timer
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

}
