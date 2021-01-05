package org.bds.executioner;

import java.util.List;
import java.util.stream.Collectors;

import org.bds.Config;
import org.bds.osCmd.Cmd;
import org.bds.osCmd.CmdAws;
import org.bds.task.Task;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.StopInstancesRequest;

/**
 * Execute tasks in a cloud
 *
 * How tasks are run:
 * 	- A checkpointVm is created and saved to a bucket (if the task is improper)
 * 	- A startup script is created to run bds (either from the checkpoint or run a shell script with 'sys' commands)
 * 	- An instance is created, the startup script is sent as parameter (terminate on shutdown should be enforced if possible)
 * 	- The instance executes the startup script (i.e. bds executed the checkpoint or script with 'sys' commands)
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

	public static final String KILL_COMMAND[] = { "@aws_ec2_terminate" };

	protected ExecutionerCloudAws(Config config) {
		super(config);
		monitorTask = MonitorTasks.get().getMonitorTaskQueue();
	}

	@Override
	public synchronized Cmd createRunCmd(Task task) {
		task.createProgramFile(); // We must create a program file
		debug("Running task " + task.getId());
		String qurl = ((QueueThreadAwsSqs) queueThread).getQueueId();
		CmdAws cmd = new CmdAws(task, qurl);
		return cmd;
	}

	@Override
	protected void follow(Task task) {
		super.follow(task);
		getCheckTasksRunning().add(task);
	}

	@Override
	protected void followStop(Task task) {
		super.followStop(task);
		getCheckTasksRunning().remove(task);
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

	/**
	 * Kill all tasks in a list
	 */
	@Override
	protected synchronized void killAll(List<Task> tokill) {
		debug("Killing " + tokill.size() + " tasks");

		List<String> instanceIds = tokill.stream() //
				.map(t -> t.getPid()) //
				.collect(Collectors.toList());

		if (instanceIds.isEmpty()) return;

		log("Terminating instances " + instanceIds);
		StopInstancesRequest request = StopInstancesRequest.builder().instanceIds(instanceIds).build();
		Ec2Client ec2 = Ec2Client.create();
		ec2.stopInstances(request);
	}

	@Override
	protected synchronized void killTask(Task task) {
		debug("Killing task " + task.getId());
		Cmd cmd = getCmd(task);
		if (cmd != null) cmd.kill();
	}

	@Override
	public String[] osKillCommand(Task task) {
		return KILL_COMMAND;
	}

	@Override
	protected void postMortemInfo(Task task) {
		// Is there a way to collect some statistics on an instance that died recently?
		// We could try to check CloudWatch messages and metrics
	}

	@Override
	protected void runExecutionerLoopBefore() {
		// Start a new thread if needed, don't start a thread if it's already running
		if (queueThread != null) return;

		debug("Starting queue thread");
		queueThread = new QueueThreadAwsSqs(config, (MonitorTaskQueue) monitorTask, taskLogger, queueNamePrefix);
		queueThread.setVerbose(verbose);
		queueThread.setDebug(debug);
		queueThread.start();

		// Wait until the queue is created to continue.
		// Otherwise we could dispatch a task before the queue exists or
		// before we receive an error from the queue syste,)
		while (!queueThread.hasQueue() && !queueThread.hasError())
			sleepShort();

		debug("Queue created, has error: " + queueThread.hasError());

		// Any errors while creating the queue? If so, abort immediately
		if (queueThread.hasError()) throw new RuntimeException("Error creating queue!", queueThread.getError());

	}
}
