package org.bds.osCmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bds.Config;
import org.bds.cluster.host.TaskResourcesAws;
import org.bds.task.Task;
import org.bds.util.Timer;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateTagsRequest;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.RunInstancesResponse;
import software.amazon.awssdk.services.ec2.model.StopInstancesRequest;
import software.amazon.awssdk.services.ec2.model.Tag;

/**
 * Execute a command in an AWS EC2 instance.
 *
 * - Create startup script (task's sys commands or checkpoint)
 * - Find instance's parameters (type, image, disk, etc.)
 * - Run instance
 * - Store instance ID
 *
 * @author pcingola
 */
public class CmdAws extends Cmd {

	public static int START_FAIL_MAX_ATTEMPTS = 50;
	public static int START_FAIL_SLEEP_RAND_TIME = 60;

	protected Task task;
	protected String instanceId;
	protected String queueName;

	public CmdAws(Task task, String queueName) {
		super(task.getId(), null);
		this.task = task;
	}

	/**
	 * Add tags to an EC2 instance
	 */
	void addTags(Ec2Client ec2, TaskResourcesAws resources) {
		try {
			// Add bds 'taskId' tag
			List<Tag> tags = new ArrayList<>();
			Tag tag = Tag.builder().key("bds_taskId").value(task.getId()).build();
			tags.add(tag);

			// Add user tags
			for (Entry<String, String> e : resources.getTags().entrySet()) {
				Tag.builder().key(e.getKey()).value(e.getValue()).build();
			}

			// Attach tags to instance
			CreateTagsRequest tagRequest = CreateTagsRequest.builder().resources(instanceId).tags(tags).build();
			ec2.createTags(tagRequest);
			if (Config.get().isVerbose()) Timer.showStdErr("Started AWS EC2 instance '" + instanceId + "'");
		} catch (Ec2Exception e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
	}

	/**
	 * Create an AWS EC2 instance
	 */
	protected String createEC2Instance() {
		Ec2Client ec2 = Ec2Client.create();
		TaskResourcesAws resources = (TaskResourcesAws) task.getResources();
		RunInstancesRequest runRequest = resources.ec2InstanceRequest(); // Create instance request
		runInstance(ec2, runRequest); // Run the instance
		addTags(ec2, resources); // Add tags
		return instanceId;
	}

	@Override
	protected void execCmd() throws Exception {
		// TODO: Wait for the instance to finish and store exit value

	}

	@Override
	protected boolean execPrepare() throws Exception {
		// TODO: Create the script
		// TODO: Create startup script (task's sys commands or checkpoint)
		// TODO: Find instance's parameters (type, image, disk, etc.)

		// Create and run instance
		createEC2Instance();

		// TODO: If START_FAILED, sleep random time, iterate N times
		// res

		return true;
	}

	@Override
	protected void killCmd() {
		if (debug) log("Terminating instance '" + instanceId + "'");
		StopInstancesRequest request = StopInstancesRequest.builder().instanceIds(instanceId).build();
		Ec2Client ec2 = Ec2Client.create();
		ec2.stopInstances(request);
	}

	/**
	 * Try to create and run an instance.
	 *
	 * If fails, make START_FAIL_MAX_ATTEMPTS, sleeping a random time between attempts
	 * The reason is that sometimes AWS does not have availability of a specific instance
	 * type at the moment, so we need to wait for instances to become available.
	 *
	 * TODO: Add support for selecting multiple zones / regions (sequentially or randomly?)
	 *
	 * @param ec2
	 * @param runRequest
	 * @return
	 */
	protected RunInstancesResponse runInstance(Ec2Client ec2, RunInstancesRequest runRequest) {
		// Run the instance
		RunInstancesResponse response = ec2.runInstances(runRequest);

		Random rand = new Random();
		for (int i = 0; i < START_FAIL_MAX_ATTEMPTS; i++) {
			// Success?
			if (response.hasInstances()) {
				//				// Start instance: Not needed?
				//				StartInstancesRequest request = StartInstancesRequest.builder().instanceIds(instanceId).build();
				//				StartInstancesResponse res = ec2.startInstances(request);
				instanceId = response.instances().get(0).instanceId();
				return response;
			}

			// Create instance failed. Sleep for a random time and re-try
			System.err.println("Create instance failed. Response: " + response);
			int millis = 1000 * rand.nextInt(START_FAIL_SLEEP_RAND_TIME);
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		// Too many attempts
		throw new RuntimeException("Unable to create instance ");
	}

}
