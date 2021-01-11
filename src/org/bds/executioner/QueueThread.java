package org.bds.executioner;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bds.BdsLog;
import org.bds.Config;
import org.bds.task.Task;
import org.bds.util.Gpr;

/**
 * A thread that handles queue messages
 *
 * @author pcingola
 */
public abstract class QueueThread extends Thread implements BdsLog {

	protected Config config;
	protected boolean debug;
	protected Exception error;
	protected boolean verbose;
	protected MonitorTaskQueue monitorTasks; // Monitor tasks: This object checks if a task finished. In this case, the messages from the queue inform this monitor about finished tasks
	protected boolean running; // Is this thread runing?
	protected TaskLogger taskLogger; // Add and remove queue ID from TaskLogger, so `bds-exec` can properly clean up if needed
	protected Map<String, Task> taskById;
	protected Map<String, OutputStream> stdErrByTaskId;
	protected Map<String, OutputStream> stdOutByTaskId;

	public static String decode64(String s) {
		return new String(Base64.getDecoder().decode(s));
	}

	public QueueThread(Config config, MonitorTaskQueue monitorTasks, TaskLogger taskLogger) {
		verbose = config.isVerbose();
		debug = config.isDebug();
		this.monitorTasks = monitorTasks;
		this.taskLogger = taskLogger;
		taskById = new HashMap<>();
		stdErrByTaskId = new HashMap<>();
		stdOutByTaskId = new HashMap<>();
	}

	public void add(Task task) {
		if (task == null) return;
		debug("Adding task id '" + task.getId() + "'");
		taskById.put(task.getId(), task);
	}

	/**
	 * Close all file descriptors, delete queue
	 */
	protected void close() {
		debug("Close");
		// Remove all tasks (close all open streams)
		List<Task> tasks = new ArrayList<>();
		tasks.addAll(taskById.values());
		debug("Removing " + tasks.size() + " pending tasks");
		for (Task t : tasks)
			remove(t);

		// Delete the queue and add a "removed" entry in TaskLogger
		if (deleteQueue()) {
			if (taskLogger != null) {
				taskLogger.remove(getQueueId());
				taskLogger = null;
			}
		}
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
	 * Write to STDOUT and to 'stdout' file
	 */
	protected void exit(String msg, Task task) {
		if (task == null) return;

		// Write exit file
		Gpr.toFile(task.getExitCodeFile(), msg);

		// Tell monitor that this task finished
		if (monitorTasks != null) monitorTasks.addFinished(task);
	}

	public Exception getError() {
		return error;
	}

	/**
	 * Create a unique ID used to uniquely identify the queue
	 */
	public abstract String getQueueId();

	public boolean hasError() {
		return error != null;
	}

	protected abstract boolean hasQueue();

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
		running = false;
		close();
	}

	/**
	 * OS command to to delete task queue
	 * This is used by the 'Go' bds command to delete queues when
	 * java process is killed (see TaskLogger class)
	 */
	public abstract String osDeleteQueueCommand();

	/**
	 * Process a single message
	 */
	protected void processMessage(String message) {
		String[] parts = message.split("\t");
		String taskId = parts[0];
		String stdout = parts.length > 1 ? parts[1] : "";
		String stderr = parts.length > 2 ? parts[2] : "";
		String sexit = parts.length > 3 ? parts[3] : "";

		Task task = taskById.get(taskId);
		if (task == null) {
			System.err.println("WARNING: Recieved message for unknown task ID '" + taskId + "'");
		}

		// Show message to console
		if (!stdout.isEmpty()) {
			stdout = decode64(stdout);
			stdout(stdout, task);
		}

		if (!stderr.isEmpty()) {
			stderr = decode64(stderr);
			stderr(stderr, task);
		}

		if (!sexit.isEmpty()) {
			sexit = decode64(sexit);
			exit(sexit, task);
		}
	}

	public synchronized void remove(Task task) {
		debug("Removing task id '" + task.getId() + "'");
		taskById.remove(task.getId());

		// Close stderr output stream
		try {
			OutputStream os = stdErrByTaskId.get(task.getId());
			if (os != null) {
				os.flush();
				os.close();
			}
		} catch (IOException e) {
			throw new RuntimeException("Error closing file '" + task.getStderrFile() + "'");
		}

		// Close stderr output stream
		try {
			OutputStream os = stdOutByTaskId.get(task.getId());
			if (os != null) {
				os.flush();
				os.close();
			}
		} catch (IOException e) {
			throw new RuntimeException("Error closing file '" + task.getStdoutFile() + "'");
		}

	}

	/**
	 * Run thread: Run executioner's main loop
	 */
	@Override
	public void run() {
		debug("Started running queue thread");
		runQueue();
		debug("Finished running queue thread");
	}

	/**
	 * Main queue loop: Read messages from the queue and process them
	 */
	protected abstract void runLoop();

	/**
	 * Run after the thread is stopped
	 */
	protected void runLoopAfter() {
		debug("After queue thread");
		close();
	}

	/**
	 * Run before starting the main loop
	 */
	protected void runLoopBefore() {
		debug("Creating queue");

		// Create the queue and add an entry in TaskLogger
		if (createQueue()) {
			if (taskLogger != null) taskLogger.add(getQueueId(), osDeleteQueueCommand());
		} else {
			// Error creating queue, cannot run
			running = false;
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
			debug("runQueue finished: running=" + running);
			running = false;
			runLoopAfter(); // Clean up
		}
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Write to STDERR and to 'stderr' file
	 */
	protected void stderr(String msg, Task task) {
		System.err.print(msg);
		System.err.flush();
		if (task == null) return;

		// Get output stream, or create a new one
		String tid = task.getId();
		OutputStream os = stdErrByTaskId.get(tid);
		if (os == null) {
			try {
				debug("Creating SDTERR file '" + task.getStderrFile() + "'");
				os = new BufferedOutputStream(new FileOutputStream(task.getStderrFile()));
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Error opening file '" + task.getStderrFile() + "'", e);
			}
			stdErrByTaskId.put(tid, os);
		}

		// Append to output stream
		try {
			os.write(msg.getBytes());
		} catch (IOException e) {
			throw new RuntimeException("Error writing to file '" + task.getStderrFile() + "'", e);
		}
	}

	/**
	 * Write to STDOUT and to 'stdout' file
	 */
	protected void stdout(String msg, Task task) {
		System.out.print(msg);
		System.out.flush();
		if (task == null) return;

		// Get output stream, or create a new one
		String tid = task.getId();
		OutputStream os = stdOutByTaskId.get(tid);
		if (os == null) {
			try {
				debug("Creating STDOUT file '" + task.getStdoutFile() + "'");
				os = new BufferedOutputStream(new FileOutputStream(task.getStdoutFile()));
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Error opening file '" + task.getStdoutFile() + "'", e);
			}
			stdOutByTaskId.put(tid, os);
		}

		// Append to output stream
		try {
			os.write(msg.getBytes());
		} catch (IOException e) {
			throw new RuntimeException("Error writing to file '" + task.getStdoutFile() + "'", e);
		}
	}

}
