package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.util.ArrayList;
import java.util.HashMap;

import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task.TaskState;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Monitor a task: Check if a task finished by checking if 'exitFile' exists
 * 
 * @author pcingola
 */
public class MonitorTask {

	// Cluster scheduling is usually quite slow, so we don't need a short monitoring interval.
	// Reducing this sleep time adds processing and probably has not many benefits. 
	public static final int SLEEP_TIME = 500;

	boolean debug = false;
	boolean verbose;
	HashMap<Task, Executioner> execByTask;
	Timer latestUpdate;

	//	boolean running;

	public MonitorTask() {
		execByTask = new HashMap<Task, Executioner>();
		latestUpdate = new Timer();
	}

	/**
	 * Add a task to this 'timer'
	 * 
	 * @param executioner : Executioner executing this task (we must be able to kill the task)
	 * @param task : Task (timeout is inferred from task.resources)
	 */
	public synchronized void add(Executioner executioner, Task task) {
		if (debug) Gpr.debug("MonitorExitFile: Adding task " + task.getId());
		if (task == null) return;
		execByTask.put(task, executioner);
	}

	//	public void kill() {
	//		running = false;
	//	}

	/**
	 * Run once every SLEEP_TIME
	 */
	public synchronized void check() {
		// Is it time to update?
		if (latestUpdate.elapsed() < SLEEP_TIME) return;

		update();
		latestUpdate.start();
	}

	//	@Override
	//	public void run() {
	//		for (running = true; running;) {
	//			update();
	//			sleep();
	//		}
	//	}

	/**
	 * Remove task (do not monitor)
	 * @param task
	 */
	public synchronized void remove(Task task) {
		if (debug) Gpr.debug("MonitorExitFile: Removing task " + task.getId());
		execByTask.remove(task);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	//	/**
	//	 * Sleep a short time
	//	 */
	//	void sleep() {
	//		try {
	//			sleep(SLEEP_TIME);
	//		} catch (InterruptedException e) {
	//		}
	//	}

	/**
	 * Check is an exist file exists, update states
	 */
	synchronized void update() {
		ArrayList<Task> toUpdate = null;

		for (Task task : execByTask.keySet()) {
			String exitFile = task.getExitCodeFile();

			if (Gpr.exists(exitFile) || task.isTimedOut()) {
				// Create (or add) to tasks to delete
				if (toUpdate == null) toUpdate = new ArrayList<Task>();
				toUpdate.add(task);
			}
		}

		Gpr.debug("Update:" + (toUpdate == null ? 0 : toUpdate.size()));

		// An task to delete?
		if (toUpdate != null) {
			for (Task task : toUpdate) {
				update(task);
				remove(task); // We don't need to monitor this task any more
			}
		}
	}

	/**
	 * Exit file exists: update states
	 */
	synchronized void update(Task task) {
		if (debug) Gpr.debug("MonitorExitFile: Found exit file " + task.getExitCodeFile());

		int exitVal = 0;
		TaskState taskState = null;

		if (task.isTimedOut()) {
			// Timed out
			exitVal = Task.EXITCODE_TIMEOUT;
			taskState = TaskState.ERROR_TIMEOUT;
		} else {
			// Exit file found: Parse exit file
			// sleep();
			String exitFileStr = Gpr.readFile(task.getExitCodeFile()).trim();
			exitVal = (exitFileStr.equals("0") ? 0 : 1); // Anything else than OK is error condition
			taskState = null; // Automatic: let taskFinished decide
			if (debug) Gpr.debug("MonitorExitFile: Task finished '" + task.getId() + "', exit status : '" + exitFileStr + "', exit code " + exitVal);
		}

		// Inform executioner that task has finished
		Executioner executioner = execByTask.get(task);
		executioner.taskFinished(task, taskState, exitVal);
	}
}
