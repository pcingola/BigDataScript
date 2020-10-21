package org.bds.executioner;

import java.util.HashMap;
import java.util.Map;

import org.bds.Config;
import org.bds.cluster.ComputerSystem;
import org.bds.cluster.host.Host;
import org.bds.cluster.host.HostLocal;
import org.bds.osCmd.Cmd;
import org.bds.osCmd.Exec;
import org.bds.task.Tail;
import org.bds.task.Task;

/**
 * An ExecutionerFileSystem is an abstract system that executes Tasks.
 *
 * In this type of executioner, there is a common/shared file system.
 * This means that processes can write the results, STDOUT,
 * STDERR and exit status to files and the executioner would be able to
 * access that information immediately by just looking in the same path.
 *
 * Examples of these systems are:
 * 	- A single somputer / server: Obviously in a single computer all the
 *    processes can read / write to the (shared) disk
 *
 *  - A cluster: In a typical cluster, all nodes write to a shared file
 *    system
 *
 *  - Computer in a campus, with a shared file system
 *
 *
 * @author pcingola
 */
public abstract class ExecutionerFileSystem extends Executioner implements PidParser {

	private Map<String, Cmd> cmdById; // Command indexed by taskID
	protected int hostIdx = 0;
	protected Tail tail; // Perform a "tail -f" of STDOUT/STDERR files from all tasks, i.e. read the latest lines and show them on the local console

	public ExecutionerFileSystem(Config config) {
		super(config);
		tail = config.getTail();
		cmdById = new HashMap<>();

		// Create a cluster having only one host (this computer)
		system = new ComputerSystem();
		new HostLocal(system); // Host is added to the system in constructor
	}

	protected synchronized void addCmd(Task task, Cmd cmd) {
		cmdById.put(task.getId(), cmd);
	}

	@Override
	protected void checkFinishedTasks() {
		if (monitorTask != null) monitorTask.check();
	}

	/**
	 * Create a command form a task
	 */
	public synchronized Cmd createRunCmd(Task task) {
		throw new RuntimeException("Unimplemented method for class: " + getClass().getCanonicalName());
	}

	/**
	 * Start following a running task (e.g. tail STDOUT & STDERR)
	 */
	@Override
	protected synchronized void follow(Task task) {
		if (taskLogger != null) taskLogger.add(task, this); // Log PID (if any)

		tail.add(task.getStdoutFile(), false);
		tail.add(task.getStderrFile(), true);

		if (monitorTask != null) monitorTask.add(this, task); // Start monitoring exit file
	}

	/**
	 * Stop following a running task (e.g. tail STDOUT & STDERR)
	 */
	@Override
	protected synchronized void followStop(Task task) {
		tail.remove(task.getStdoutFile());
		tail.remove(task.getStderrFile());

		// Remove from loggers
		if (taskLogger != null) taskLogger.remove(task);
		if (monitorTask != null) monitorTask.remove(task);
	}

	protected synchronized Cmd getCmd(Task task) {
		return cmdById.get(task.getId());
	}

	/**
	 * Kill a task
	 */
	@Override
	protected synchronized void killTask(Task task) {
		// Kill command
		Cmd cmd = getCmd(task);
		if (cmd != null) cmd.kill();
	}

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
	@Override
	protected void postMortemInfo(Task task) {
		// Nothing to do
	}

	/**
	 * Remove a command (task)
	 */
	protected synchronized void removeCmd(Task task) {
		cmdById.remove(task.getId());
	}

	/**
	 * Run a task on a given host. I.e. execute command to run
	 * @param task : Task to run
	 * @param host : Host to run task (can be null)
	 */
	@Override
	protected void runTask(Task task, Host host) {
		if (config.getMaxThreads() > 0) {
			// Don't run too many threads at once
			// The reason for this is that we can reach the maximum
			// number of threads available in the operating system.
			// If that happens, well get an pendingException
			while (Exec.countRunningThreads() >= config.getMaxThreads()) {
				// Too many threads running? Sleep for a while (block until some threads finish)
				if (debug) log("INFO: Too many threads running (limit set to " + config.getMaxThreads() + "). Waiting for some threads to finish.");
				sleepLong();
			}
		}

		Cmd cmd = createRunCmd(task);
		if (cmd != null) {
			addCmd(task, cmd);
			cmd.setHost(host);
			cmd.setExecutioner(this);
			cmd.setTask(task);
			cmd.setDebug(debug);
		}

		host.add(task);
		if (cmd != null) cmd.start();

		// Wait some milliseconds?
		int waitTime = config.getWaitAfterTaskRun();
		if (waitTime > 0) {
			try {
				sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Task finished (either finished OK or has some error condition)
	 */
	@Override
	protected synchronized void taskUpdateFinishedCleanUp(Task task) {
		// Find command
		Cmd cmd = getCmd(task);
		if (cmd != null) {
			Host host = cmd.getHost();
			remove(task, host); // Remove task form host
		}
		removeCmd(task); // Remove command (if any)
	}
}
