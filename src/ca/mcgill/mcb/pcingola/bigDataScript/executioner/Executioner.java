package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.util.ArrayList;
import java.util.HashMap;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostLocal;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Cmd;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Tail;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task.TaskState;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Tuple;

/**
 * An Executioner is an abstract system that executes several Tasks.
 * It can represent a computer, cluster, cloud, etc.
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
	protected ArrayList<Task> tasksToRun; // Tasks queued for execution
	protected HashMap<Task, Host> tasksSelected; // Tasks that has been selected and it will be immediately start execution in host 
	protected HashMap<String, Task> tasksRunning; // Tasks running
	protected HashMap<String, Task> tasksDone; // Tasks that fin
	protected HashMap<String, Cmd> cmdById;
	protected Tail tail;
	protected PidLogger pidLogger;
	protected Config config;
	protected Cluster cluster; // Local computer is the 'server' (localhost)

	public Executioner(Config config) {
		super();
		this.config = config;
		tasksToRun = new ArrayList<Task>();
		tasksSelected = new HashMap<Task, Host>();
		tasksRunning = new HashMap<String, Task>();
		tasksDone = new HashMap<String, Task>();
		tail = config.getTail();
		pidLogger = config.getPidLogger();
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
	public void add(Task task) {
		if (debug) Timer.showStdErr("Queuing task '" + task.getId() + "'");
		tasksToRun.add(task);
	}

	protected void add(Task task, Host host) {
		tasksSelected.put(task, host);
		host.add(task);
	}

	/**
	 * Create a command form a task
	 * @param task
	 * @return
	 */
	protected abstract Cmd createCmd(Task task);

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
	 * @param task
	 */
	protected void follow(Task task) {
		if (pidLogger != null) pidLogger.add(task, this); // Log PID (if any)

		tail.add(task.getStdoutFile(), null, false);
		tail.add(task.getStderrFile(), null, true);
	}

	/**
	 * Stop following a running task (e.g. tail STDOUT & STDERR)
	 * @param task
	 */
	protected void followStop(Task task) {
		tail.remove(task.getStdoutFile());
		tail.remove(task.getStderrFile());

		// Remove from loggers
		if (pidLogger != null) pidLogger.remove(task);
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
		// Kill all 'tasksToRun'.
		// Note: We need to create a new list to avoid concurrent modification exceptions 
		ArrayList<Task> tokill = new ArrayList<Task>();
		tokill.addAll(tasksToRun);
		tokill.addAll(tasksRunning.values());
		for (Task t : tokill)
			kill(t);

		running = false;
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
	public void kill(Task task) {
		if (debug) Timer.showStdErr("Killing task '" + task.getId() + "'");

		// Kill command
		Cmd cmd = cmdById.get(task.getId());
		if (cmd != null) cmd.kill();

		// Mark task as finished
		// Note: This will also be invoked by Cmd, so it will be redundant)
		taskFinished(task, TaskState.KILLED, Task.EXITCODE_KILLED);
	}

	/**
	 * Return the appropriate 'kill' command to be used by the OS
	 * E.g.: For a local task it would be 'kill' whereas for a cluster task it would be 'qdel'
	 * 
	 * @param task
	 * @return
	 */
	public abstract String osKillCommand(Task task);

	protected void remove(Task task, Host host) {
		tasksSelected.remove(task);
		host.remove(task);
	}

	/**
	 * Run thread: Run executioner's main loop
	 */
	@Override
	public void run() {
		runExecutioner();
	}

	/**
	 * Run task queues
	 */
	public void runExecutioner() {
		if (debug) Timer.showStdErr("Starting " + this.getClass().getSimpleName());
		running = true;

		runExecutionerLoopBefore(); // Initialize, before run loop

		try {
			// Run until killed
			while (running) {
				// Run loop
				if (runExecutionerLoop()) {
					if (debug) Timer.showStdErr("Queue: No task to run.");
				}
				sleepLong();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException(t);
		} finally {

			runExecutionerLoopAfter(); // Clean up
		}
	}

	/**
	 * Run loop 
	 * @return true if all processes have finished executing
	 */
	protected boolean runExecutionerLoop() {
		// Nothing to run?
		if (!hasTaskToRun()) return false;

		// Are there any more task to run?
		while (running && hasTaskToRun()) {
			// Select a task to run
			Tuple<Task, Host> taskHostPair = selectTask();

			// Any task selected?
			if (taskHostPair != null) {
				// Get next task and run it
				runTask(taskHostPair.first, taskHostPair.second);
			} else {
				sleepShort();
				if (debug) Timer.showStdErr("Queue tasks:\tPending : " + tasksToRun.size() + "\tRunning: " + tasksRunning.size() + "\tDone: " + tasksDone.size());
			}
		}

		return true;
	}

	/**
	 * Clean up after run loop
	 */
	protected void runExecutionerLoopAfter() {
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
		if (debug) Timer.showStdErr("Running task '" + task.getId() + "' on host " + host.getHostName());

		Cmd cmd = createCmd(task);
		cmd.setHost(host);
		cmd.setExecutioner(this);
		cmd.setTask(task);
		cmd.setDebug(debug);

		cmdById.put(task.getId(), cmd);

		host.add(task);
		cmd.start();
	}

	/**
	 * Select next task to run and which host it should run into
	 * @return
	 */
	protected Tuple<Task, Host> selectTask() {
		// Nothing to run?
		if (tasksToRun.isEmpty()) return null;

		for (Task task : tasksToRun) {
			// Already selected? Skip
			if (tasksSelected.containsKey(task)) continue;

			// Can we run this task? 
			if (task.canRun()) {
				// Select host (we only have one)
				for (Host host : cluster) {

					// Do we have enough resources to run this task in this host?
					if (host.getResources().hasResources(task.getResources())) {
						// OK, execute this task in this host						
						add(task, host); // Add task to host (make sure resources are reserved)
						return new Tuple<Task, Host>(task, host);
					}
				}
			}
		}

		// Cannot run any task in any host
		return null;
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
	public synchronized void taskFinished(Task task, TaskState taskState, int exitValue) {
		if (task == null) throw new RuntimeException("Task finished invoked with null task. This should never happen.");

		String id = task.getId();
		if (debug) Timer.showStdErr("Finished task '" + id + "'");

		// Set exit status 
		task.setExitValue(exitValue);

		// Find command
		Cmd cmd = cmdById.get(id);
		if (cmd != null) {
			Host host = cmd.getHost();
			remove(task, host); // Remove task form host
			cmdById.remove(id); // Remove command
		}

		followStop(task); // Remove from 'tail' thread

		// Move from 'running' (or 'toRun') to 'done'
		tasksToRun.remove(task.getId());
		tasksRunning.remove(task.getId());
		tasksDone.put(task.getId(), task);

		// Schedule removal of TMP files (if not logging)
		if (!log) task.deleteOnExit();

		// Set task state. Infer form exit code if no state is available.
		// Note: This is the last thing we do in order for wait() methods to be sure that task has finished and all data has finished updating.
		if (taskState == null) taskState = TaskState.exitCode2taskState(exitValue);
		task.state(taskState);
	}

	/**
	 * Move a task from 'tasksToRun' to 'tasksRunning'
	 * @param task
	 */
	public synchronized void taskRunning(Task task) {
		if (debug) Timer.showStdErr("Task running '" + task.getId() + "'");

		// Change state
		task.state(TaskState.RUNNING);

		// Follow STDOUT and STDERR
		follow(task);
	}

	public synchronized void taskStarted(Task task) {
		if (debug) Timer.showStdErr("Task started '" + task.getId() + "'");

		// Move from 'tasksToRun' to 'tasksRunning'
		tasksToRun.remove(task);
		tasksSelected.remove(task);
		tasksRunning.put(task.getId(), task);

		// Change state
		task.state(TaskState.STARTED);
	}

	@Override
	public String toString() {
		return "Queue type: '" + this.getClass().getSimpleName() + "'\tPending : " + tasksToRun.size() + "\tRunning: " + tasksRunning.size() + "\tDone: " + tasksDone.size();
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
