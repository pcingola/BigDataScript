package org.bds.executioner;

import org.bds.Config;
import org.bds.util.Gpr;
import org.bds.util.Timer;

/**
 * A thread that handles queue messages
 *
 * @author pcingola
 */
public abstract class QueueThread extends Thread {

	protected Config config;
	protected boolean debug;
	protected boolean verbose;
	protected MonitorTaskQueue monitorTasks; // Monitor tasks: This object checks if a task finished. In this case, the messages from the queue inform this monitor about finished tasks
	protected boolean running; // Is this thread runing?
	protected TaskLogger taskLogger; // Add and remove queue ID from TaskLogger, so `bds-exec` can properly clean up if needed

	public QueueThread(Config config, MonitorTaskQueue monitorTasks, TaskLogger taskLogger) {
		verbose = config.isVerbose();
		debug = config.isDebug();
		this.monitorTasks = monitorTasks;
		this.taskLogger = taskLogger;
	}

	/**
	 * Create queue: Return true if the queue was created, false otherwise
	 */
	public abstract boolean createQueue();

	/**
	 * Delete queue: Return true if the queue was deleted, false otherwise
	 */
	public abstract boolean deleteQueue();

	/**
	 * Create a unique ID used to uniquely identify the queue
	 */
	public abstract String getQueueId();

	/**
	 * Is this executioner running?
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Stop queue thread
	 */
	public synchronized void kill() {
		// TODO: Interrupt thread to make sure it wakes up from waiting messages
		Gpr.debug("!!! TODO: Interrupt thread to make sure it wakes up from waiting messages");
		running = false;
	}

	public void log(String msg) {
		Timer.showStdErr(getClass().getSimpleName() + ": " + msg);
	}

	/**
	 * OS command to to delete task queue
	 * This is used by the 'Go' bds command to delete queues when
	 * java process is killed (see TaskLogger class)
	 */
	public abstract String osDeleteQueueCommand();

	protected void processMessage(String message) {
		// TODO: Write STDOUT / STDERR to local files and show on console

		// TODO: Write exitCode to local file AND invoke monitorTasks.addFinished(task)
	}

	/**
	 * Run thread: Run executioner's main loop
	 */
	@Override
	public void run() {
		if (debug) log("Started running");
		runQueue();
		if (debug) log("Finished running");
	}

	/**
	 * Main queue loop: Read messages from the queue and process them
	 */
	protected abstract void runLoop();

	/**
	 * Run after the thread is stopped
	 */
	protected void runLoopAfter() {
		// Delete the queue and add a "removed" entry in TaskLogger
		if (deleteQueue()) {
			if (taskLogger != null) taskLogger.remove(getQueueId());
		}
	}

	/**
	 * Run before starting the main loop
	 */
	protected void runLoopBefore() {
		// Create the queue and add an entry in TaskLogger
		if (createQueue()) {
			if (taskLogger != null) taskLogger.add(getQueueId(), osDeleteQueueCommand());
		}
	}

	/**
	 * Run executioner's main loop
	 */
	public void runQueue() {
		running = true;

		runLoopBefore(); // Initialize, before run loop

		try {
			// Run until killed
			while (running) {
				runLoop();
			}
		} catch (Throwable t) {
			running = false;
			t.printStackTrace();
			throw new RuntimeException(t);
		} finally {
			running = false;
			runLoopAfter(); // Clean up
		}
	}

}
