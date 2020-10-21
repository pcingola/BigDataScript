package org.bds.executioner;

import org.bds.Config;
import org.bds.util.Timer;

/**
 * A thread that handles queue messages
 *
 * @author pcingola
 */
public class QueueThread extends Thread {

	protected Config config;
	protected boolean debug;
	protected boolean verbose;
	protected MonitorTaskQueue monitorTasks; // Monitor tasks: This object checks if a task finished. In this case, the messages from the queue inform this monitor about finished tasks
	protected boolean running;
	protected TaskLogger taskLogger;

	public QueueThread(Config config, MonitorTaskQueue monitorTasks, TaskLogger taskLogger) {
		verbose = config.isVerbose();
		debug = config.isDebug();
		this.monitorTasks = monitorTasks;
	}

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
		running = false;
	}

	public void log(String msg) {
		Timer.showStdErr(getClass().getSimpleName() + ": " + msg);
	}

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
	protected void runLoop() {
		// TODO: Receive messages

		// TODO: Process messages
	}

	protected void runLoopAfter() {
		monitorTasks.deleteQueue();
		taskLogger.remove(monitorTasks.getQueueName());
	}

	protected void runLoopBefore() {
		monitorTasks.createQueue();
		taskLogger.add(monitorTasks.getQueueName());
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
