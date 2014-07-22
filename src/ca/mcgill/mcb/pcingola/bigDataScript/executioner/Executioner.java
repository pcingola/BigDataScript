package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostLocal;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Cmd;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Exec;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Tail;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task.DependencyState;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task.TaskState;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.TextTable;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Tuple;

/**
 * An Executioner is an abstract system that executes several Tasks.
 * It can represent a computer, cluster, cloud, etc.
 *
 * @author pcingola
 */
public abstract class Executioner extends Thread implements NotifyTaskState, PidParser {

	public static final int SLEEP_TIME_LONG = 500;
	public static final int SLEEP_TIME_MID = 200;
	public static final int SLEEP_TIME_SHORT = 10;
	public static final int REPORT_INTERVAL = 60;

	protected boolean debug;
	protected boolean verbose;
	protected boolean log;
	protected boolean running, valid;
	protected int hostIdx = 0;
	protected ArrayList<Task> tasksToRun; // Tasks queued for execution
	protected HashMap<Task, Host> tasksSelected; // Tasks that has been selected and it will be immediately start execution in host
	protected HashMap<String, Task> tasksRunning; // Tasks running
	protected HashMap<String, Task> tasksDone; // Tasks that fin
	protected LinkedList<Tuple<Task, TaskState>> taskUpdateStates; // Tasks to be updated
	protected HashMap<String, Cmd> cmdById;
	protected Tail tail;
	protected Config config;
	protected TaskLogger taskLogger;
	protected MonitorTask monitorTask;
	protected Cluster cluster; // Local computer is the 'server' (localhost)
	protected Timer timer; // Task timer (when was the task started)
	protected CheckTasksRunning checkTasksRunning;

	public Executioner(Config config) {
		super();
		valid = true;
		this.config = config;
		tasksToRun = new ArrayList<Task>();
		tasksSelected = new HashMap<Task, Host>();
		tasksRunning = new HashMap<String, Task>();
		tasksDone = new HashMap<String, Task>();
		taskUpdateStates = new LinkedList<Tuple<Task, TaskState>>();
		tail = config.getTail();
		taskLogger = config.getTaskLogger();
		cmdById = new HashMap<String, Cmd>();
		debug = config.isDebug();
		verbose = config.isVerbose();

		// Create a cluster having only one host (this computer)
		cluster = new Cluster();
		new HostLocal(cluster);
	}

	/**
	 * Queue an Exec and return a the id
	 * @return
	 */
	public synchronized void add(Task task) {
		if (verbose) log("Queuing task: " + task.getId());
		tasksToRun.add(task);
	}

	/**
	 * Create a command form a task
	 * @param task
	 * @return
	 */
	protected synchronized Cmd createCmd(Task task) {
		throw new RuntimeException("Unimplemented method for class: " + getClass().getCanonicalName());
	}

	/**
	 * Find a task by ID
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
	 * Start following a running task (e.g. tail STDOUT & STDERR)
	 */
	protected synchronized void follow(Task task) {
		if (taskLogger != null) taskLogger.add(task, this); // Log PID (if any)

		tail.add(task.getStdoutFile(), false);
		tail.add(task.getStderrFile(), true);

		if (monitorTask != null) monitorTask.add(this, task); // Start monitoring exit file
	}

	/**
	 * Stop following a running task (e.g. tail STDOUT & STDERR)
	 */
	protected synchronized void followStop(Task task) {
		 tail.remove(task.getStdoutFile());
		 tail.remove(task.getStderrFile());

		// Remove from loggers
		if (taskLogger != null) taskLogger.remove(task);
		if (monitorTask != null) monitorTask.remove(task);
	}

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

	public HashMap<String, Task> getTasksRunning() {
		return tasksRunning;
	}

	/**
	 * Any task running?
	 * @return
	 */
	public synchronized boolean hasTaskRunning() {
		return tasksRunning.size() > 0;
	}

	/**
	 * Are there any tasks either running or to be run?
	 * @return
	 */
	public synchronized boolean hasTaskToRun() {
		return tasksToRun.size() - tasksSelected.size() > 0;
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
	 * Should we show a report?
	 * @return
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
	 * @return
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
		if (verbose) log("Killed");

		// Kill all 'tasksToRun'.
		// Note: We need to create a new list to avoid concurrent modification exceptions
		ArrayList<Task> tokill = new ArrayList<Task>();
		tokill.addAll(tasksToRun);
		tokill.addAll(tasksRunning.values());
		for (Task t : tokill)
			kill(t);

		running = valid = false;
	}

	/**
	 * Kill task by ID
	 * @param taskId
	 */
	public void kill(String taskId) {
		Task t = findTask(taskId);
		if (t != null) kill(t);
	}

	/**
	 * Kill a task and move it from 'taskRunning' to 'tasksDone'
	 * @param task
	 * @return
	 */
	public synchronized void kill(Task task) {
		if (task.isDone()) return; // Nothing to do

		if (debug) log("Killing task '" + task.getId() + "'");

		// Kill command
		Cmd cmd = cmdById.get(task.getId());
		if (cmd != null) cmd.kill();

		// Mark task as finished
		// Note: This will also be invoked by Cmd, so it will be redundant)
		task.setExitValue(Task.EXITCODE_KILLED);
		taskFinished(task, TaskState.KILLED);
	}

	public void log(String msg) {
		Timer.showStdErr(getClass().getSimpleName() + " '" + getExecutionerId() + "': " + msg);
	}

	/**
	 * Return the appropriate 'kill' command to be used by the OS
	 * E.g.: For a local task it would be 'kill' whereas for a cluster task it would be 'qdel'
	 */
	public abstract String[] osKillCommand(Task task);

	/**
	 * Parse PID line from 'bds exec' (Cmd)
	 */
	@Override
	public String parsePidLine(String line) {
		return line.trim();
	}

	/**
	 * Try to find some 'post-mortem' info about this
	 * task, in order to asses systematic errors.
	 */
	protected void postMortemInfo(Task task) {
		// Nothing to do
	}

	protected synchronized void remove(Task task, Host host) {
		tasksSelected.remove(task);
		host.remove(task);
	}

	/**
	 * Perform reports, checks and state updates
	 */
	protected void reportsChecksUpdates() {
		taskUpdateStates();

		// Check if tasks finished running
		if (monitorTask != null) monitorTask.check();

		// Report tasks
		reportTasks();

		// Check that task are still running
		if (getCheckTasksRunning() != null) getCheckTasksRunning().check();

	}

	/**
	 * Report tasks to running, to run and done
	 */
	protected void reportTasks() {
		// if (verbose &&
		if ((hasTaskRunning() || hasTaskToRun()) && isReportTime()) {
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
				if (t.getDependency() != null) {
					for (Task td : t.getDependency())
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
			Timer.showStdErr("Tasks [" + getExecutionerId() + "]\t\tPending : " + tasksToRun.size() + "\tRunning: " + tasksRunning.size() + "\tDone: " + tasksDone.size() + "\n" + tt.toString());
		}
	}

	/**
	 * Run thread: Run executioner's main loop
	 */
	@Override
	public void run() {
		if (verbose) log("Started running");
		runExecutioner();
		if (verbose) log("Finished running");
	}

	/**
	 * Run task queues
	 */
	public void runExecutioner() {
		running = true;

		runExecutionerLoopBefore(); // Initialize, before run loop

		try {
			// Run until killed
			while (running) {
				// Run loop
				if (runExecutionerLoop()) {
					if (debug) log("Queue: No task to run.");
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
	 * Run a task on a given host. I.e. execute command
	 * @param task : Task to run
	 * @param host : Host to run task (can be null)
	 * @return
	 */
	protected void runTask(Task task, Host host) {
		// Don't run too many threads at once
		// The reason for this is that we can reach the maximum
		// number of threads available in the operating system.
		// If that happens, well get an exception
		for (int numThreads = Exec.countRunningThreads(); numThreads >= Exec.MAX_NUMBER_OF_RUNNING_THREADS; numThreads = Exec.countRunningThreads()) {
			// Too many threads running? Sleep for a while (block until some threads finish)
			if (verbose) log("INFO: Too many threads running (" + numThreads + "). Waiting for some threads to finish.");
			sleepLong();
		}

		// TODO: If an exception is thrown here, we should be able to either recover or mark the task as START_FAILED
		Cmd cmd = createCmd(task);
		if (cmd != null) {
			cmd.setHost(host);
			cmd.setExecutioner(this);
			cmd.setTask(task);
			cmd.setDebug(debug);
		}

		cmdById.put(task.getId(), cmd);

		host.add(task);
		if (cmd != null) cmd.start();
	}

	/**
	 * Select next task to run and which host it should run into
	 * @return
	 */
	protected synchronized Tuple<Task, Host> selectTask() {
		// Nothing to run?
		if (tasksToRun.isEmpty()) return null;

		LinkedList<Task> finishTask = null;

		// Try to find a task matching a host
		for (Task task : tasksToRun) {
			// Already selected? Skip
			if (tasksSelected.containsKey(task)) continue;

			boolean canBeExecuted = false;

			// Can we run this task?
			if (task.canRun()) {
				//---
				// Are dependencies satisfied?
				//---
				DependencyState dep = task.dependencyState();
				switch (dep) {
				case OK:
					break;

				case WAIT:
					continue;

				case ERROR:
					// Dependency error => Finish this task
					if (finishTask == null) finishTask = new LinkedList<Task>();
					finishTask.add(task);
					continue; // Do not schedule

				default:
					throw new RuntimeException("Unimplemented dependency state '" + dep + "'");

				}

				//---
				// Select host (we only have one)
				//---
				for (Host host : cluster) {
					// Host is not alive?
					if (!host.getHealth().isAlive()) {
						canBeExecuted = true; // May be this host can actually execute this task, we don't know.
						continue;
					}

					// Do we have enough resources to run this task in this host?
					if (host.getResourcesAvaialble().hasResources(task.getResources())) {
						// OK, execute this task in this host
						selectTask(task, host); // Add task to host (make sure resources are reserved)
						return new Tuple<Task, Host>(task, host);
					} else if (!canBeExecuted) {
						// Can any host actually run this task?
						canBeExecuted = host.getResources().hasResources(task.getResources());
					}
				}

				//---
				// There is no host that can execute this task?
				//---
				if (!canBeExecuted) {
					Gpr.debug("Cluster info: " + cluster.info());
					Gpr.debug("Cluster: " + cluster);
					task.setErrorMsg("Not enough resources to execute task: " + task.getResources());

					// Mark the task to be finished (cannot be done here due to concurrent modification)
					if (finishTask == null) finishTask = new LinkedList<Task>();
					finishTask.add(task);
				}
			}
		}

		// Finish these tasks. Cannot be executed: failed due to resources issues.
		if (finishTask != null) {
			for (Task task : finishTask) {
				task.setExitValue(Task.EXITCODE_ERROR);
				taskFinished(task, TaskState.START_FAILED);
			}
			finishTask = null;
		}

		// Cannot run any task in any host
		return null;
	}

	/**
	 * Select task to be executed on a host
	 */
	protected synchronized void selectTask(Task task, Host host) {
		if (verbose) log("Task selected '" + task.getId() + "' on host '" + host + "'");
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
	 * @param id
	 * @return
	 */
	@Override
	public synchronized void taskFinished(Task task, TaskState taskState) {
		if (taskState == null) {
			// Set task state. Infer form exit code if no state is available.
			// Note: This is the last thing we do in order for wait() methods to be sure that task has finished and all data has finished updating.
			// Set exit status
			taskState = TaskState.exitCode2taskState(task.getExitValue());
		}

		taskUpdateStates.add(new Tuple<Task, TaskState>(task, taskState));
	}

	/**
	 * Move a task from 'tasksToRun' to 'tasksRunning'
	 * @param task
	 */
	@Override
	public synchronized void taskRunning(Task task) {
		taskUpdateStates.add(new Tuple<Task, TaskState>(task, TaskState.RUNNING));

	}

	@Override
	public synchronized void taskStarted(Task task) {
		taskUpdateStates.add(new Tuple<Task, TaskState>(task, TaskState.STARTED));
	}

	protected synchronized void taskUpdateFinished(Task task, TaskState taskState) {
		if (task == null) throw new RuntimeException("Task finished invoked with null task. This should never happen.");

		String id = task.getId();
		if (debug) log("Task finished '" + id + "'");

		// Find command
		Cmd cmd = cmdById.get(id);
		if (cmd != null) {
			Host host = cmd.getHost();
			remove(task, host); // Remove task form host
			cmdById.remove(id); // Remove command
		}

		followStop(task); // Remove from 'tail' thread

		// Move from 'running' (or 'toRun') to 'done'
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
				log("Task failed, retrying ( " + task.getMaxFailCount() + " remaining retries ): task ID '" + task.getId() + "'" + (verbose ? "\n" : ", ") + task.toString(verbose));

				// Move task form 'taskDone' back to 'tasksToRun' queue
				task.reset(); // Prepare to re-run task
				tasksDone.remove(task.getId());
				tasksToRun.add(task);
			} else {
				// May be we can look for additional information to asses the error
				postMortemInfo(task);
			}
		}
	}

	protected synchronized void taskUpdateRunning(Task task) {
		if (debug) log("Task running '" + task.getId() + "'");

		if (task.isDone()) return; // Already finished, nothing to do

		// Change state
		task.state(TaskState.RUNNING);

		// Follow STDOUT and STDERR
		follow(task);
	}

	/**
	 * Task has been started
	 * @param task
	 */
	protected synchronized void taskUpdateStarted(Task task) {
		if (debug) log("Task started '" + task.getId() + "'");

		if (task.isStarted()) return; // Already started, nothing to do

		// Move from 'tasksToRun' to 'tasksRunning'
		tasksToRun.remove(task);
		tasksSelected.remove(task);
		tasksRunning.put(task.getId(), task);

		// Change state
		task.state(TaskState.STARTED);
	}

	/**
	 * Update task states
	 */
	protected synchronized void taskUpdateStates() {
		if (taskUpdateStates.isEmpty()) return;

		// Update each task sequentially, to avoid race conditions
		for (Tuple<Task, TaskState> taskAndState : taskUpdateStates) {
			Task task = taskAndState.first;
			TaskState state = taskAndState.second;

			if (state.isStarted()) taskUpdateStarted(task);
			else if (state.isRunning()) taskUpdateRunning(task);
			else taskUpdateFinished(task, state);
		}

		taskUpdateStates = new LinkedList<Tuple<Task, TaskState>>();
	}

	@Override
	public String toString() {
		return "Executioner : '" + getExecutionerId() + "'\tPending : " + tasksToRun.size() + "\tRunning: " + tasksRunning.size() + "\tDone: " + tasksDone.size();
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
