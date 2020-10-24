package org.bds.executioner;

import java.util.List;

import org.bds.Config;
import org.bds.cluster.ComputerSystem;
import org.bds.cluster.host.HostInifinte;
import org.bds.task.Task;
import org.bds.util.Gpr;

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
 * 	- Instace are terminated
 *
 *
 * @author pcingola
 */
public abstract class ExecutionerCloud extends Executioner {

	protected QueueThread queueThread;

	protected ExecutionerCloud(Config config) {
		super(config);

		// Create a system having only one host with 'infinite' capacity (cloud)
		system = new ComputerSystem();
		new HostInifinte(system);
	}

	@Override
	protected void follow(Task task) {
		if (taskLogger != null) taskLogger.add(task, this); // Log PID (if any)
		if (monitorTask != null) monitorTask.add(this, task); // Start monitoring exit file
	}

	@Override
	protected void followStop(Task task) {
		// Remove from loggers
		if (taskLogger != null) taskLogger.remove(task);
		if (monitorTask != null) monitorTask.remove(task);
	}

	/**
	 * Stop executioner and kill all tasks
	 */
	@Override
	public synchronized void kill() {
		super.kill();
		if (queueThread != null) {
			queueThread.kill();
			queueThread = null;
		}
	}

	/**
	 * Kill all tasks in a list
	 */
	@Override
	protected synchronized void killAll(List<Task> tokill) {
		// TODO: Terminate all instance in one API call
		Gpr.debug("UNIMPLEMENTED !!!");
	}

	@Override
	protected synchronized void killTask(Task task) {
		// TODO: Terminate instance
		Gpr.debug("UNIMPLEMENTED !!!");
	}

	@Override
	protected void runExecutionerLoopBefore() {
		// Start a new thread if needed, don't start a thread if it's already running
		if (queueThread == null) {
			queueThread = new QueueThreadAwsSqs(config, (MonitorTaskQueue) monitorTask, taskLogger);
			queueThread.start();
		}
	}

}
