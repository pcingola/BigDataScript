package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.util.ArrayList;
import java.util.HashMap;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * A system that can execute an Exec
 * 
 * @author pcingola
 */
public abstract class Executioner extends Thread {

	protected boolean debug = true;
	protected boolean verbose = true;
	protected boolean running;
	protected int hostIdx = 0;
	ArrayList<Task> tasksToRun;
	HashMap<String, Task> tasksDone, tasksRunning;

	public Executioner() {
		super();
		tasksToRun = new ArrayList<Task>();
		tasksDone = new HashMap<String, Task>();
		tasksRunning = new HashMap<String, Task>();
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
	 * Task finished executing
	 * @param id
	 * @return
	 */
	public synchronized boolean finished(String id) {
		if (verbose) Timer.showStdErr("Finished task '" + id + "'");
		Task task = tasksRunning.get(id);
		if (task == null) {
			if (debug) Timer.showStdErr("Finished task: ERROR, cannot find task '" + id + "'");
			return false;
		}

		// Move from 'running' to 'done'
		tasksRunning.remove(task);
		tasksDone.put(id, task);

		return true;
	}

	public synchronized boolean hasTaskToRun() {
		return !tasksToRun.isEmpty() || !tasksRunning.isEmpty();
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
	public void kill() {
		running = false;

		// Kill all tasks
		ArrayList<Task> tokill = new ArrayList<Task>();
		tokill.addAll(tasksRunning.values());
		for (Task t : tokill)
			kill(t.getId());
	}

	/**
	 * Kill a task 
	 * 
	 * @param id : Task id
	 * @return true if it was killed
	 */
	public boolean kill(String id) {
		if (verbose) Timer.showStdErr("Killing task '" + id + "'");

		Task task = tasksRunning.get(id);
		if (task == null) {
			// Try tasks to run
			if (remove(id)) return true;

			// No such task
			if (verbose) Timer.showStdErr("Killing task: ERROR, cannot find task '" + id + "'");
			return false;
		}

		// Running task? Kill and move to 'done' 
		boolean ok = killTask(task);
		finished(id);

		return ok;
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
			if (tasksToRun.get(i).getId().equals(id)) delete = i;

		// Remove task number
		if (delete > 0) {
			tasksToRun.remove(delete);
			return true;
		}
		return false;
	}

	/**
	 * Run a task
	 * @param task
	 * @return
	 */
	public synchronized boolean run(Task task) {
		if (verbose) Timer.showStdErr("Running task '" + task.getId() + "'");
		boolean ok = runTask(task);
		if (ok) {
			tasksRunning.put(task.getId(), task);
		} else if (verbose) Timer.showStdErr("Running task: ERROR, could not run task '" + task.getId() + "'");
		return ok;
	}

	/**
	 * Run a task
	 * @param task
	 * @return
	 */
	protected abstract boolean runTask(Task task);

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

}
