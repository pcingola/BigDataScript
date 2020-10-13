package org.bds.executioner;

import org.bds.Config;
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

	protected ExecutionerCloudAws(Config config) {
		super(config);
	}

	@Override
	public String[] osKillCommand(Task task) {
		// TODO: Add command to kill instance
		throw new RuntimeException("UNIMPLEMENTED!!!!");
		//		return null;
	}

}
