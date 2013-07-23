package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.util.ArrayList;
import java.util.HashMap;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * A system that can execute an Exec
 * 
 * @author pcingola
 */
public abstract class Executioner extends Thread {

	public static final int SLEEP_TIME_LONG = 500;
	public static final int SLEEP_TIME_SHORT = 10;

	protected boolean debug;
	protected boolean verbose;
	protected boolean log;
	protected boolean running;
	protected int hostIdx = 0;
	protected ArrayList<Task> tasksToRun;
	protected HashMap<String, Task> tasksDone, tasksRunning;
	protected Tail tail;
	PidLogger pidLogger;

	public Executioner(PidLogger pidLogger) {
		super();
		this.pidLogger = pidLogger;
		tasksToRun = new ArrayList<Task>();
		tasksDone = new HashMap<String, Task>();
		tasksRunning = new HashMap<String, Task>();
		tail = new Tail();
	}

	/**
	 * Queue an Exec and return a the id
	 * @return
	 */
	public void add(Task task) {
		if (verbose) Timer.showStdErr("Queuing task '" + task.getId() + "'");
		tasksToRun.add(task);
	}

	/**
	 * Add to 'Tail'. Follow STDOUT & STDERR
	 * @param task
	 */
	protected void addTail(Task task) {
		tail.add(task.getStdoutFile(), null, false);
		tail.add(task.getStderrFile(), null, true);
	}

	/**
	 * Get a task
	 * @param id
	 * @return
	 */
	public synchronized Task findTask(String id) {
		Task t = tasksRunning.get(id);
		if (t != null) return t;

		t = tasksDone.get(id);
		if (t != null) return t;

		for (Task tt : tasksToRun)
			if (tt.getId().equals(id)) return tt;

		return null;
	}

	/**
	 * Task finished executing
	 * @param id
	 * @return
	 */
	public boolean finished(String id) {
		Task task = tasksRunning.get(id);
		if (task == null) return false;
		if (verbose) Timer.showStdErr("Finished task '" + id + "'");

		removeTail(task); // Remove from 'tail' thread
		finished(task); // Move to 'done'
		if (pidLogger != null) pidLogger.remove(task);

		return true;
	}

	/**
	 * Move a task from 'tasksRunning' to 'tasksDone'
	 * @param task
	 */
	protected synchronized void finished(Task task) {
		if (task == null) return;
		tasksRunning.remove(task.getId());
		tasksDone.put(task.getId(), task);

		// Schedule removal of TMP files (if not logging)
		if (!log) task.deleteOnExit();
	}

	/**
	 * Are there any tasks either running or to be run?
	 * @return
	 */
	public synchronized boolean hasTaskToRun() {
		return !tasksToRun.isEmpty() || !tasksRunning.isEmpty();
	}

	/**
	 * Has any task failed?
	 * @return
	 */
	public boolean isFailed() {
		for (Task t : tasksDone.values())
			if (!t.isCanFail() && t.isFailed()) return true; // A task that cannot fail, failed!
		return false;
	}

	/**
	 * Is this executioner running?
	 * @return
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Is this executioner valid?
	 * An Executioner may expire or become otherwise invalid
	 * 
	 * @return
	 */
	public boolean isValid() {
		return isRunning();
	}

	/**
	 * Stop executioner and kill all tasks
	 */
	public synchronized void kill() {
		// Kill all 'tasksToRun': Move them to 'tasksDone' and set exitCode to 1
		ArrayList<Task> tokill = new ArrayList<Task>();
		tokill.addAll(tasksToRun);
		for (Task t : tokill) {
			running(t);
			t.stateError();
			finished(t);
		}

		// Kill all running tasks
		tokill = new ArrayList<Task>();
		tokill.addAll(tasksRunning.values());
		for (Task t : tokill)
			kill(t);
	}

	/**
	 * Kill a task 
	 * 
	 * @param id : Task id
	 * @return true if it was killed
	 */
	public synchronized boolean kill(String id) {
		if (verbose) Timer.showStdErr("Killing task '" + id + "'");

		// Running?
		Task task = tasksRunning.get(id);
		if (task != null) return kill(task);

		// To run?
		for (int i = 0; i < tasksToRun.size(); i++) {
			Task t = tasksToRun.get(i);
			if (t.getId().equals(id)) {
				running(t);
				t.stateError();
				finished(t);
				return true;
			}
		}

		return false;
	}

	/**
	 * Kill a task and move it from 'taskRunning' to 'tasksDone'
	 * @param task
	 * @return
	 */
	public boolean kill(Task task) {
		boolean ok = killTask(task);
		finished(task);
		return ok;
	}

	/**
	 * An OS command to kill this task
	 * @param task
	 * @return
	 */
	public String killCommand(Task task) {
		return "";
	}

	/**
	 * Kill a task
	 */
	protected abstract boolean killTask(Task task);

	/**
	 * Remove a task from the list of tasks to run
	 * @param id
	 * @return
	 */
	public synchronized boolean remove(String id) {
		// Find task number
		int delete = -1;
		for (int i = 0; (i < tasksToRun.size()) && (delete < 0); i++)
			if (tasksToRun.get(i).getId().equals(id)) {
				delete = i;
				tasksToRun.remove(delete);
			}

		// Not found
		if (delete < 0) return false;

		return true;
	}

	/**
	 * Remove from 'Tail'. Stop following STDOUT & STDERR
	 * @param task
	 */
	protected void removeTail(Task task) {
		tail.remove(task.getStdoutFile());
		tail.remove(task.getStderrFile());
	}

	/**
	 * Run task queues
	 */
	@Override
	public void run() {
		if (debug) Timer.showStdErr("Starting " + this.getClass().getSimpleName());
		running = true;

		runLoopBefore(); // Initialize, before run loop

		try {
			// Run until killed
			while (running) {
				// Run loop
				if (runLoop()) {
					if (debug) Timer.showStdErr("Queue: No task to run.");
				}

				sleepLong();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException(t);
		} finally {
			runLoopAfter(); // Clean up
		}
	}

	/**
	 * Run loop 
	 * @return true if all processes have finished executing
	 */
	protected abstract boolean runLoop();

	/**
	 * Clean up after run loop
	 */
	protected void runLoopAfter() {
		tail.kill(); // Kill tail process
	}

	/**
	 * Initialize before run loop
	 */
	protected void runLoopBefore() {
		tail.start(); // Create a 'tail' process (to show STDOUT & STDERR from all processes)
	}

	/**
	 * Move a task from 'tasksToRun' to 'tasksRunning'
	 * @param task
	 */
	protected synchronized void running(Task task) {
		if (task == null) return;
		tasksToRun.remove(task);
		tasksRunning.put(task.getId(), task);
	}

	/**
	 * Run a task on a given host
	 * @param task : Task to run
	 * @param host : Host to run task (can be null)
	 * @return
	 */
	public synchronized boolean runTask(Task task, Host host) {
		if (verbose) Timer.showStdErr("Running task '" + task.getId() + "'");

		boolean ok = runTaskCommand(task, host);
		if (ok) {
			waitStart(task); // Wait until task started. Otherwise we might get race conditions ('tail' starts reading stdout before readPid)

			if (task.isFailed()) runTaskFailed(task);
			else runTaskStarted(task);

		} else {
			// Error running
			if (verbose) Timer.showStdErr("Running task: ERROR, could not run task '" + task.getId() + "'");
		}

		return ok;
	}

	/**
	 * Run a task (execute commands)
	 * @param task
	 * @return
	 */
	protected abstract boolean runTaskCommand(Task task, Host host);

	/**
	 * This method is called when a task failed starting
	 * @param task
	 */
	protected void runTaskFailed(Task task) {
		if (debug) Gpr.debug("Task failed to initialize " + task.getId());
		// Switch task to finished state
		running(task);
		finished(task);
	}

	/**
	 * Get next task to run and run it (on 'host')
	 * @param host : Host where this task should be run (can be null)
	 * @return True if run 
	 */
	protected synchronized boolean runTaskNext(Host host) {
		if (tasksToRun.isEmpty()) return false;
		return runTask(tasksToRun.get(0), host); // Run first task on the list
	}

	/**
	 * This method is called after a task was successfully started 
	 * @param task
	 */
	protected void runTaskStarted(Task task) {
		running(task); // Set task to running state
		addTail(task); // Follow STDOUT and STDERR
		if (pidLogger != null) pidLogger.add(task, this); // Log PID (if any)
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
		//		taskTimer.setVerbose(verbose);
	}

	void sleepLong() {
		try {
			sleep(SLEEP_TIME_LONG);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	void sleepShort() {
		try {
			sleep(SLEEP_TIME_SHORT);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "Queue type: " + this.getClass().getSimpleName() + "\tPending : " + tasksToRun.size() + "\tRunning: " + tasksRunning.size() + "\tDone: " + tasksDone.size();
	}

	/**
	 * Wait for a task to start
	 * @param task
	 */
	protected void waitStart(Task task) {
		// Busy wait, probably not the smartest thing to do...
		while (!task.isStarted())
			sleepShort();
	}

}
