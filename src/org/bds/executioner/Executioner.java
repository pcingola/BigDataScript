package org.bds.executioner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bds.Config;
import org.bds.cluster.ComputerSystem;
import org.bds.cluster.host.Host;
import org.bds.run.BdsThread;
import org.bds.task.DependencyState;
import org.bds.task.Task;
import org.bds.task.TaskState;
import org.bds.util.Gpr;
import org.bds.util.TextTable;
import org.bds.util.Timer;
import org.bds.util.Tuple;

/**
 * An Executioner is an abstract system that executes Tasks.
 *
 * @author pcingola
 */
public abstract class Executioner extends Thread implements NotifyTaskState {

	public static final int REPORT_INTERVAL = 60; // Interval in seconds

	public static final int SLEEP_TIME_LONG = 500; // Milliseconds
	public static final int SLEEP_TIME_MID = 200; // Milliseconds
	public static final int SLEEP_TIME_SHORT = 10; // Milliseconds

	protected CheckTasksRunning checkTasksRunning;
	protected Config config;
	protected boolean debug;
	protected LinkedList<Task> finishTask; // These tasks should be marked as finished in the next update iteration
	protected boolean log;
	protected MonitorTask monitorTask; // Monitor tasks: This object checks if a task finished (e.g. by checking if 'exitFile' exists)
	protected boolean removeTaskCannotExecute; // Should a task be finished if there are no resources to execute it? In most cases yes, but some clusters host are dynamic (they appear and disappear), so even if there are no resources now there might be resources in the future.
	protected boolean running, valid;
	protected Map<String, Task> tasksDone; // Tasks that finished
	protected Map<String, Task> tasksRunning; // Tasks running
	protected Map<Task, Host> tasksSelected; // Tasks that has been selected and it will be immediately start execution in host
	protected List<Task> tasksToRun; // Tasks queued for execution
	protected List<Tuple<Task, TaskState>> taskUpdateStates; // Tasks to be updated
	protected ComputerSystem system; // A representation of the "system" processing the data (a server, cluster, etc.)
	protected boolean verbose;
	protected TaskLogger taskLogger; // Log tasks into parent (Go) bds program for cleanup (kill task, delete files, etc)
	protected Timer timer; // Task timer (when was the task started)

	public Executioner(Config config) {
		super();
		valid = true;
		this.config = config;
		tasksToRun = new ArrayList<>();
		taskUpdateStates = new ArrayList<>();
		tasksSelected = new HashMap<>();
		tasksRunning = new HashMap<>();
		tasksDone = new HashMap<>();
		removeTaskCannotExecute = true;
		taskLogger = config.getTaskLogger();
		verbose = config.isVerbose();
		debug = config.isDebug();
		log = config.isLog();
	}

	/**
	 * Queue an Exec and return a the id
	 */
	public synchronized void add(Task task) {
		if (debug) log("Queuing task: " + task.getId());
		task.state(TaskState.SCHEDULED);
		tasksToRun.add(task);
	}

	/**
	 * Check if any tasks have finished
	 */
	protected abstract void checkFinishedTasks();

	/**
	 * Check is tasks are running
	 */
	protected void checkTasksRunning() {
		if (getCheckTasksRunning() != null) getCheckTasksRunning().check();
	}

	/**
	 * Count the number of failed tasks
	 */
	int countFaield() {
		int count = 0;
		for (Map.Entry<String, Task> entry : tasksDone.entrySet()) {
			if (entry.getValue().isFailed()) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Find a task by ID
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

	protected abstract void follow(Task task);

	protected abstract void followStop(Task task);

	/**
	 * Return a CheckTasksRunning object that can check tasks or null if
	 * there is independent way to check
	 */
	protected CheckTasksRunning getCheckTasksRunning() {
		return null;
	}

	public String getExecutionerId() {
		return getExecutionerName() + "[" + getId() + "]";
	}

	public String getExecutionerName() {
		String execStr = Executioner.class.getSimpleName();
		String exName = getClass().getSimpleName();
		if (exName.startsWith(execStr)) exName = exName.substring(execStr.length());
		return exName;
	}

	public ComputerSystem getSystem() {
		return system;
	}

	public synchronized List<String> getTaskIdsDone() {
		List<String> tids = new ArrayList<>();
		tids.addAll(tasksDone.keySet());
		return tids;
	}

	public synchronized List<String> getTaskIdsRunning() {
		List<String> tids = new ArrayList<>();
		tids.addAll(tasksRunning.keySet());
		return tids;
	}

	public synchronized List<String> getTaskIdsToRun() {
		List<String> tids = new ArrayList<>();
		for (Task t : tasksToRun)
			tids.add(t.getId());
		return tids;
	}

	public synchronized List<Task> getTasksRunning() {
		List<Task> tasks = new ArrayList<>();
		tasks.addAll(tasksRunning.values());
		return tasks;
	}

	/**
	 * Any task running?
	 */
	public synchronized boolean hasTaskRunning() {
		return tasksRunning.size() > 0;
	}

	/**
	 * Are there any tasks either running or to be run?
	 */
	public synchronized boolean hasTaskToRun() {
		return tasksToRun.size() - tasksSelected.size() > 0;
	}

	/**
	 * Has any task failed?
	 */
	public boolean isFailed() {
		for (Task t : tasksDone.values())
			if (!t.isCanFail() && t.isFailed()) return true; // A task that cannot fail, failed!
		return false;
	}

	/**
	 * Should we show a report?
	 */
	protected boolean isReportTime() {
		if (timer == null) timer = new Timer();
		if (timer.elapsedSecs() > REPORT_INTERVAL) {
			timer.start(); // Restart timer
			return true;
		}
		return false;
	}

	/**
	 * Is this executioner running?
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Is this executioner valid?
	 * An Executioner may expire or become otherwise invalid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Stop executioner and kill all tasks
	 */
	public synchronized void kill() {
		if (debug) log("Killed");

		// Kill all 'tasksToRun'.
		// Note: We need to create a new list to avoid concurrent modification exceptions
		List<Task> tokill = new ArrayList<>();
		tokill.addAll(tasksToRun);
		tokill.addAll(tasksRunning.values());
		killAll(tokill);
		running = valid = false;
	}

	/**
	 * Kill task by ID
	 */
	public void kill(String taskId) {
		Task t = findTask(taskId);
		if (t != null) kill(t);
	}

	/**
	 * Kill a task and move it from 'taskRunning' to 'tasksDone'
	 */
	public synchronized void kill(Task task) {
		if (task.isDone()) return; // Nothing to do

		if (debug) log("Killing task '" + task.getId() + "'");

		killTask(task);

		// Mark task as finished
		// Note: This will also be invoked by Cmd, so it will be redundant)
		task.state(TaskState.KILLED);
		task.setExitValue(BdsThread.EXITCODE_KILLED);
		taskFinished(task, TaskState.KILLED);
	}

	/**
	 * Kill all tasks in a list
	 */
	protected synchronized void killAll(List<Task> tokill) {
		for (Task t : tokill)
			kill(t);
	}

	/**
	 * Kill a task
	 * @param task
	 */
	protected abstract void killTask(Task task);

	public void log(String msg) {
		Timer.showStdErr(getClass().getSimpleName() + " '" + getExecutionerId() + "': " + msg);
	}

	/**
	 * Return the appropriate 'kill' command to be used by the OS
	 * E.g.: For a local task it would be 'kill' whereas for a cluster task it would be 'qdel'
	 */
	public abstract String[] osKillCommand(Task task);

	/**
	 * Try to find some 'post-mortem' info about this
	 * task, in order to asses systematic errors.
	 */
	protected abstract void postMortemInfo(Task task);

	/**
	 * Remove a task form a host
	 */
	protected synchronized void remove(Task task, Host host) {
		tasksSelected.remove(task);
		host.remove(task);
	}

	/**
	 * Perform reports, checks and state updates
	 */
	protected void reportsChecksUpdates() {
		taskUpdateStates(); // Update task states
		checkFinishedTasks(); // Check if tasks finished running
		reportTasks(); // Report tasks (show to console)
		checkTasksRunning(); // Perform an independent check that task are still running (e.g. query the cluster system)
	}

	/**
	 * Report tasks to running, to run and done
	 * This line is shown on the console every one minute, when in verbose mode
	 */
	protected void reportTasks() {
		if ((hasTaskRunning() || hasTaskToRun()) && isReportTime()) {
			log("Tasks [" + getExecutionerId() + "]" //
					+ "\t\tPending: " + tasksToRun.size() //
					+ "\tRunning: " + tasksRunning.size() //
					+ "\tDone: " + tasksDone.size() //
					+ "\tFailed: " + countFaield() //
					+ (Config.get().isVerbose() ? "\n" + toStringTable() : "") //
			);
		}
	}

	/**
	 * Run thread: Run executioner's main loop
	 */
	@Override
	public void run() {
		if (debug) log("Started running");
		runExecutioner();
		if (debug) log("Finished running");
	}

	/**
	 * Run executioner's main loop
	 */
	public void runExecutioner() {
		running = true;

		runExecutionerLoopBefore(); // Initialize, before run loop

		try {
			// Run until killed
			while (running) {
				// Run loop
				if (runExecutionerLoop()) {
					if (debug) log("Queue: No more tasks to run.");
				}

				sleepLong();
			}
		} catch (Throwable t) {
			running = valid = false;
			t.printStackTrace();
			kill(); // Make sure all tasks are either killed or marked as failed
			throw new RuntimeException(t);
		} finally {
			running = valid = false;
			runExecutionerLoopAfter(); // Clean up
		}
	}

	/**
	 * Run loop
	 * @return true if all processes have finished executing
	 */
	protected boolean runExecutionerLoop() {
		// Nothing to run?
		if (!hasTaskToRun()) {
			reportsChecksUpdates();
			return false;
		}

		// Are there any more task to run?
		while (running && hasTaskToRun()) {
			// Are executioner frozen?
			if (Executioners.getInstance().isFreeze()) {
				sleepMid();
				continue;
			}

			// Select a task to run
			Tuple<Task, Host> taskHostPair = selectTask();

			// Any task selected?
			if (taskHostPair != null) {
				// Get next task and run it
				runTask(taskHostPair.first, taskHostPair.second);
			} else {
				sleepMid();
			}

			reportsChecksUpdates();
		}

		return true;
	}

	/**
	 * Clean up after run loop
	 */
	protected void runExecutionerLoopAfter() {
		reportsChecksUpdates(); // Make sure all tasks states are updated
	}

	/**
	 * Initialize before run loop
	 */
	protected void runExecutionerLoopBefore() {
	}

	/**
	 * Run a task on a given host
	 * @param task : Task to run
	 * @param host : Host to run task (can be null)
	 */
	protected abstract void runTask(Task task, Host host);

	/**
	 * Select next task to run and assign host.
	 * Note: Some clusters can be viewed as having "single host with almost infinite capacity", so
	 *       the task is always assigned to the first host in the cluster (representing the
	 *       master). Then the cluster management system decides on which slave to run. So in
	 *       many clusters the deciding where to run is trivial.
	 */
	protected synchronized Tuple<Task, Host> selectTask() {
		// Nothing to run?
		if (tasksToRun.isEmpty()) return null;

		finishTask = null;

		// Try to find a task matching a host
		for (Task task : tasksToRun) {
			// Already selected? Skip
			if (tasksSelected.containsKey(task)) continue;

			// Can we run this task?
			if (task.canRun()) {
				// Are dependencies satisfied for this task?
				DependencyState dep = task.dependencyState();
				switch (dep) {
				case OK:
					break;

				case WAIT:
					continue;

				case ERROR:
					// Dependency error => Finish this task
					if (finishTask == null) finishTask = new LinkedList<>();
					finishTask.add(task);
					continue; // Do not schedule

				default:
					throw new RuntimeException("Unimplemented dependency state '" + dep + "'");

				}

				// Select a suitable host in the cluster that satisfies task resources
				Tuple<Task, Host> taskHost = selectTask(task);
				if (taskHost != null) return taskHost;
			}
		}

		// These tasks cannot be executed due to "lack of resources"
		if (finishTask != null) {
			for (Task task : finishTask) {
				task.setExitValue(BdsThread.EXITCODE_ERROR);
				taskFinished(task, TaskState.START_FAILED);
			}
			finishTask = null;
		}

		// Cannot run any task in any host
		return null;
	}

	/**
	 * Select a suitable host for this task
	 */
	protected synchronized Tuple<Task, Host> selectTask(Task task) {
		boolean canBeExecuted = false;

		//---
		// Select the first host in the cluster that satisfies requirements
		//---
		for (Host host : system) {
			// Host is not alive?
			if (!host.isAlive()) {
				canBeExecuted = true; // May be this host can actually execute this task, we don't know.
				continue;
			}

			// Do we have enough resources to run this task in this host?
			if (host.getResourcesAvaialble().hasResources(task.getResources())) {
				// OK, execute this task in this host
				if (debug) log("Selected task:" //
						+ "\n\ttask ID        : " + task.getId() //
						+ "\n\ttask hint      : " + task.getProgramHint()//
						+ "\n\ttask resources : " + task.getResources() //
						+ "\n\thost           : " + host //
						+ "\n\thost resources : " + host.getResourcesAvaialble() //
				);

				selectTask(task, host); // Add task to host (make sure resources are reserved)
				return new Tuple<>(task, host);
			} else if (!canBeExecuted) {
				// Can any host actually run this task?
				canBeExecuted = host.getResources().hasResources(task.getResources());
			}
		}

		//---
		// There is no host that can execute this task?
		//---
		if (removeTaskCannotExecute && !canBeExecuted) {
			if (debug) Gpr.debug("Cluster info: " + system.info() + "\n\tCluster: " + system);
			task.setErrorMsg("Not enough resources to execute task: " + task.getResources());

			// Mark the task to be finished (cannot be done here due to concurrent modification)
			if (finishTask == null) finishTask = new LinkedList<>();
			finishTask.add(task);
		}

		return null;
	}

	/**
	 * Select task to be executed on a host
	 */
	protected synchronized void selectTask(Task task, Host host) {
		if (debug) log("Task selected '" + task.getId() + "' on host '" + host + "'");
		tasksSelected.put(task, host);
		host.add(task);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	void sleepLong() {
		try {
			sleep(SLEEP_TIME_LONG);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	void sleepMid() {
		try {
			sleep(SLEEP_TIME_SHORT);
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

	/**
	 * Task finished executing
	 */
	@Override
	public synchronized void taskFinished(Task task, TaskState taskState) {
		if (taskState == null) {
			// Set task state. Infer form exit code if no state is available.
			// Note: This is the last thing we do in order for wait() methods to
			//       be sure that task has finished and all data has finished
			//       updating.

			taskState = task.taskState();
		}

		taskUpdateStates.add(new Tuple<>(task, taskState));
	}

	/**
	 * Move a task from 'tasksToRun' to 'tasksRunning'
	 */
	@Override
	public synchronized void taskRunning(Task task) {
		taskUpdateStates.add(new Tuple<>(task, TaskState.RUNNING));

	}

	@Override
	public synchronized void taskStarted(Task task) {
		taskUpdateStates.add(new Tuple<>(task, TaskState.STARTED));
	}

	/**
	 * Task finished (either finished OK or has some error condition).
	 * Update the state
	 */
	protected synchronized boolean taskUpdateFinished(Task task, TaskState taskState) {
		if (task == null) throw new RuntimeException("Task finished invoked with null task. This should never happen.");
		if (!task.canChangeState(taskState)) return false;

		String id = task.getId();
		if (debug) log("Task finished '" + id + "'");

		// Cleanup task
		taskUpdateFinishedCleanUp(task);

		followStop(task); // Remove from 'tail' thread

		// Move from 'running' (or 'toRun'), add it to 'done'
		tasksToRun.remove(task);
		tasksSelected.remove(task);
		tasksRunning.remove(task.getId());
		tasksDone.put(task.getId(), task);

		// Schedule removal of TMP files (if not logging)
		if (!log) task.deleteOnExit();

		// Set task state
		task.state(taskState);

		// Task finished in error condition?
		if (task.isFailed()) {
			// Can we re-try?
			if (!task.isCanFail() && task.canRetry()) {
				// Retry task
				log("Task failed, retrying ( " + task.getMaxFailCount() + " remaining retries ): task ID '" + task.getId() + "'" + (debug ? "\n" : ", ") + task.toString(verbose));

				// Move task form 'taskDone' back to 'tasksToRun' queue
				task.reset(); // Prepare to re-run task
				tasksDone.remove(task.getId());
				tasksToRun.add(task);
				task.state(TaskState.SCHEDULED);
			} else {
				// May be we can look for additional information to asses the error
				postMortemInfo(task);
			}
		}

		return true;
	}

	/**
	 * Clean up when a task is changing to finished state.
	 * Perform executioner-specifi cleanup
	 * @param task
	 */
	protected abstract void taskUpdateFinishedCleanUp(Task task);

	/**
	 * Update task state to 'RUNNING'
	 */
	protected synchronized boolean taskUpdateRunning(Task task) {
		if (debug) log("Task running '" + task.getId() + "'");

		if (task.isDone()) return true; // Already finished, nothing to do

		TaskState nextState = task.isDetached() ? TaskState.DETACHED : TaskState.RUNNING;
		if (!task.canChangeState(nextState)) return false;
		task.state(nextState);

		// A "detached" task is considered to be successful right after starting, we don't follow it
		if (!task.isDetached()) follow(task); // Follow STDOUT and STDERR

		return true;
	}

	/**
	 * Task has been started: Update to 'STARTED' state
	 */
	protected synchronized boolean taskUpdateStarted(Task task) {
		if (debug) log("Task started '" + task.getId() + "'");

		if (task.isStarted()) return true; // Already started, nothing to do
		if (!task.canChangeState(TaskState.STARTED)) return false;

		// Move from 'tasksToRun' to 'tasksRunning'
		tasksToRun.remove(task);
		tasksSelected.remove(task);
		tasksRunning.put(task.getId(), task);

		// Change state
		task.state(TaskState.STARTED);
		return true;
	}

	/**
	 * Update task states
	 */
	protected synchronized void taskUpdateStates() {
		if (taskUpdateStates.isEmpty()) return;

		// Get a list ready for next iteration
		ArrayList<Tuple<Task, TaskState>> taskUpdateStatesNew = new ArrayList<>();

		// Update each task sequentially, to avoid race conditions
		for (Tuple<Task, TaskState> taskAndState : taskUpdateStates) {
			Task task = taskAndState.first;
			TaskState state = taskAndState.second;

			// Try to change state
			boolean ok = false;
			if (state.isStarted()) ok = taskUpdateStarted(task);
			else if (state.isRunning()) ok = taskUpdateRunning(task);
			else ok = taskUpdateFinished(task, state);

			// Could not change state? Keep task update
			if (!ok) taskUpdateStatesNew.add(taskAndState);
		}

		// Keep unchanged states for next time
		taskUpdateStates = taskUpdateStatesNew;
	}

	@Override
	public String toString() {
		return "Executioner : '" + getExecutionerId() //
				+ "'\tPending : " + tasksToRun.size() //
				+ "\tRunning: " + tasksRunning.size() //
				+ "\tDone: " + tasksDone.size() //
				+ "\tFailed: " + countFaield() //
		;
	}

	/**
	 * Create a table of tasks
	 */
	public synchronized String toStringTable() {
		// Create table
		int rowNum = 0;
		String table[][] = new String[tasksToRun.size() + tasksRunning.size()][5];

		// Pending
		for (Task t : tasksToRun) {
			table[rowNum][0] = t.getPid() != null ? t.getPid() : "";
			table[rowNum][1] = "pending (" + t.getTaskState() + ")";
			table[rowNum][2] = t.getName();

			// Show dependent tasks
			StringBuilder sb = new StringBuilder();
			if (t.getDependencies() != null) {
				for (Task td : t.getDependencies())
					sb.append(td.getName() + " ");
			}
			table[rowNum][3] = sb.toString();
			table[rowNum][4] = t.getProgramHint();
			rowNum++;
		}

		// Running
		for (Task t : tasksRunning.values()) {
			table[rowNum][0] = t.getPid() != null ? t.getPid() : "";
			table[rowNum][1] = "running (" + t.getTaskState() + ")";
			table[rowNum][2] = t.getName();
			table[rowNum][3] = "";
			table[rowNum][4] = t.getProgramHint();
			rowNum++;
		}

		// Show table
		String columnNames[] = { "PID", "Task state", "Task name", "Dependencies", "Task definition" };
		TextTable tt = new TextTable(columnNames, table, "\t\t");
		return tt.toString();
	}

	/**
	 * Wait for a task to start
	 */
	protected void waitStart(Task task) {
		// Busy wait, probably not the smartest thing to do...
		while (!task.isStarted())
			sleepShort();
	}

}
