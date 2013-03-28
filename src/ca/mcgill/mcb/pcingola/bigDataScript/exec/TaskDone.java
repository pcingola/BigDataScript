package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.util.HashMap;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Task done.
 * Check if a task finished, by checking if 'exitFile' exists
 * 
 * @author pcingola
 */
public class TaskDone extends Thread {

	public static boolean debug = false;
	public static final int SLEEP_TIME = 200;

	boolean verbose = false;
	HashMap<Task, Executioner> execByTask;
	boolean running;

	public TaskDone() {
		execByTask = new HashMap<Task, Executioner>();
	}

	/**
	 * Add a task to this 'timer'
	 * 
	 * @param executioner : Executioner executing this task (we must be able to kill the task)
	 * @param task : Task (timeout is inferred from task.resources)
	 */
	public synchronized void add(Executioner executioner, Task task) {
		if (debug) Gpr.debug("TaskDone: Adding task " + task.getId());
		// Sanity check
		if (task == null) return;
		execByTask.put(task, executioner);
	}

	public void kill() {
		running = false;
	}

	public synchronized void remove(Task task) {
		execByTask.remove(task);
	}

	@Override
	public void run() {
		for (running = true; running;) {
			update();
			sleep();
		}
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Sleep a short time
	 */
	void sleep() {
		try {
			sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Check is an exist file exists, update states
	 */
	void update() {
		for (Task task : execByTask.keySet()) {
			String exitFile = task.getExitCodeFile();
			if (Gpr.exists(exitFile)) update(task);

		}
	}

	/**
	 * Exit file exists: update states
	 */
	synchronized void update(Task task) {
		if (debug) Gpr.debug("Found exit file " + task.getExitCodeFile());

		// Parse exit file
		String exitFileStr = Gpr.readFile(task.getExitCodeFile()).trim();
		int exitVal = (exitFileStr.equals("0") ? 0 : 1);
		if (debug) Gpr.debug("Task finished '" + task.getId() + "', exit status : '" + exitFileStr + "', exit code " + exitVal);

		// Set task
		task.setStarted(true);
		task.setExitValue(exitVal);
		task.setDone(true);

		// Inform executioner that task has finished
		Executioner executioner = execByTask.get(task);
		executioner.finished(task.getId());

	}
}
