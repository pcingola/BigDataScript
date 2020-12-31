package org.bds.executioner;

import org.bds.Config;
import org.bds.cluster.ComputerSystem;
import org.bds.cluster.host.HostInifinte;
import org.bds.task.Task;

/**
 * Execute tasks in a cloud
 *
 * How tasks are run:
 * 	- A checkpointVm is created and saved to a bucket
 * 	- A startup script is created to run bds from the checkpoint
 * 	- An instance is created, the startup script is sent as parameter (terminate on shutdown should be enforced if possible)
 * 	- The instance executes the checkpoint
 * 	- The instance sends STDOUT / STDERR / EXIT messages via a queue (maybe a heartbeat message)
 *
 * Executioner
 * 	- The Executioner reads the queue and shows messages / updates task status
 * 	- Instances are monitored for timeout
 * 	- Instances are monitored for failure
 * 	- Instance are terminated
 *
 *
 * @author pcingola
 */
public abstract class ExecutionerCloud extends Executioner {

	public static final String EXECUTIONER_QUEUE_NAME_PREFIX_DEFAULT = "bds_";

	protected QueueThread queueThread;
	protected String queueNamePrefix;

	protected ExecutionerCloud(Config config) {
		super(config);
		queueNamePrefix = EXECUTIONER_QUEUE_NAME_PREFIX_DEFAULT;

		// Create a system having only one host with 'infinite' capacity (cloud)
		system = new ComputerSystem();
		new HostInifinte(system);
	}

	@Override
	protected void checkFinishedTasks() {
		if (monitorTask != null) monitorTask.check();
	}

	@Override
	protected void follow(Task task) {
		if (taskLogger != null) taskLogger.add(task, this); // Log PID (if any)
		if (monitorTask != null) monitorTask.add(this, task); // Start monitoring exit file
		if (queueThread != null) queueThread.add(task); // Queue thread saves STDOUT/STDERR to files and detects exit messages
	}

	@Override
	protected void followStop(Task task) {
		// Remove from loggers
		if (taskLogger != null) taskLogger.remove(task);
		if (monitorTask != null) monitorTask.remove(task);
		if (queueThread != null) queueThread.remove(task);
	}

	public String getQueueNamePrefix() {
		return queueNamePrefix;
	}

	/**
	 * Stop executioner and kill all tasks
	 */
	@Override
	public synchronized void kill() {
		super.kill();
		if (queueThread != null) {
			debug("Killing queue");
			queueThread.kill();
			queueThread = null;
		}
	}

	public void setQueueNamePrefix(String queueNamePrefix) {
		this.queueNamePrefix = queueNamePrefix;
	}

}
