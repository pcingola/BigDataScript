package org.bds.executioner;

import org.bds.Config;
import org.bds.cluster.host.Host;
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
 * 	- The Executioner reads the queue and shows messages, and saves them to the local task.stdout / task.stderr / task.exitCode files
 * 	- Updates task status when EXIT message is received
 * 	- Instances are monitored for timeout
 * 	- Instances are monitored for failure
 * 	- All instances are terminated
 *	- Queue is deleted
 *
 * @author pcingola
 */
public class ExecutionerCloudAws extends ExecutionerCloud {

	public static final String KILL_COMMAND[] = { "aws", "ec2", "terminate" };

	protected ExecutionerCloudAws(Config config) {
		super(config);
		monitorTask = MonitorTasks.get().getMonitorTaskAws();
	}

	@Override
	protected void checkFinishedTasks() {
		// TODO Auto-generated method stub

	}

	@Override
	protected CheckTasksRunning getCheckTasksRunning() {
		if (checkTasksRunning == null) {
			checkTasksRunning = new CheckTasksRunningAws(config, this);
			checkTasksRunning.setDebug(config.isDebug());
			checkTasksRunning.setVerbose(config.isVerbose());
		}
		return checkTasksRunning;
	}

	@Override
	public String[] osKillCommand(Task task) {
		Gpr.debug("UNIMPLEMENTED !!!");
		return KILL_COMMAND;
	}

	@Override
	protected void postMortemInfo(Task task) {
		// TODO: Is there a way to collect some statistics on an instance that died recently?
		// TODO: We could try to check CloudWatch messages and metrics
	}

	@Override
	protected void runTask(Task task, Host host) {
		// TODO: Is there a queue? Create queue
		//
		// TODO: Create startup script (task's sys commands or checkpoint)
		// TODO: Find instance's parameters (type, image, disk, etc.)
		// TODO: Run instance
		// TODO: Store instance ID
		//
		// TODO: follow task (add task to queue monitor)
		// TODO: Add to 'taskLogger' so that Go bds process can kill if Java dies

		Gpr.debug("UNIMPLEMENTED !!!");
	}

	@Override
	protected void taskUpdateFinishedCleanUp(Task task) {
		Gpr.debug("UNIMPLEMENTED !!!");
	}

}
