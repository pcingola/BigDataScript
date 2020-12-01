package org.bds.osCmd;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bds.Config;
import org.bds.cluster.host.TaskResourcesAws;
import org.bds.task.Task;
import org.bds.util.Gpr;
import org.bds.util.Timer;

import software.amazon.awssdk.core.exception.SdkClientException;
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
			// Add bds tags
			List<Tag> tags = new ArrayList<>();

			String tid = task.getId();
			if (tid.indexOf('/') > 0) tid = tid.substring(0, tid.indexOf('/'));
			Gpr.debug("NAME: " + tid);
			Tag tag = Tag.builder().key("Name").value("bds " + tid).build();
			tags.add(tag);

			tag = Tag.builder().key("taskId").value(task.getId()).build();
			tags.add(tag);

			if (task.getTaskName() != null) {
				tag = Tag.builder().key("taskName").value(task.getTaskName()).build();
				tags.add(tag);
			}

			// Add user tags
			Map<String, String> tagsMap = resources.getTags();
			if (tagsMap != null) {
				for (Entry<String, String> e : tagsMap.entrySet()) {
					Tag.builder().key(e.getKey()).value(e.getValue()).build();
				}
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
		TaskResourcesAws resources = (TaskResourcesAws) task.getResources();
		Ec2Client ec2 = resources.ec2Client(); // Create client
		RunInstancesRequest.Builder runRequestBuilder = resources.ec2InstanceRequest(); // Create instance request

		// Add startup script (encoded as bas64)
		String script = instanceStartupScript(resources);
		String script64 = new String(Base64.getEncoder().encode(script.getBytes()));
		runRequestBuilder.userData(script64);

		// Run the instance
		RunInstancesRequest runRequest = runRequestBuilder.build();
		runInstance(ec2, runRequest);
		addTags(ec2, resources); // Add tags

		return instanceId;
	}

	@Override
	protected void execCmd() throws Exception {
		// TODO: Wait for the instance to finish and store exit value

	}

	@Override
	protected boolean execPrepare() throws Exception {
		// TODO: Create the tasks's script
		// TODO: Create the tasks's script

		// Create and run instance
		createEC2Instance();

		return true;
	}

	/**
	 * Creates the instance's startup script
	 */
	protected String instanceStartupScript(TaskResourcesAws resources) {
		// TODO: Add shell from config file or map
		// TODO: Add bds code to run from checkpoint / run script
		StringBuilder sb = new StringBuilder();
		sb.append("#!/bin/bash -eu\n");
		sb.append("set -o pipefail\n");

		// Script finished function: Shutdown the instance, unless 'keepInstanceAliveAfterFinish' is set
		sb.append("function exit_script {\n");
		if (!resources.isKeepInstanceAliveAfterFinish()) sb.append("    shutdown -h now\n");
		else sb.append("    echo 'INFO: Keeping instance alive (keepInstanceAliveAfterFinish was set)'\n");
		sb.append("}\n");

		// Script main
		sb.append("trap exit_script EXIT\n");
		sb.append("echo \"INFO: Starting script '$0'\"\n");
		sb.append("bds -h\n");
		sb.append("echo \"INFO: Finished script '$0'\"\n");
		return sb.toString();
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
	 */
	protected RunInstancesResponse runInstance(Ec2Client ec2, RunInstancesRequest runRequest) {
		// Run the instance
		RunInstancesResponse response;
		try {
			response = ec2.runInstances(runRequest);
		} catch (SdkClientException e) {
			throw new RuntimeException("EC2 client expcetion (this might be caused by incorrectly setting the region). " + e.getMessage(), e);
		}

		Random rand = new Random();
		for (int i = 0; i < START_FAIL_MAX_ATTEMPTS; i++) {
			// Success?
			if (response.hasInstances()) {
				instanceId = response.instances().get(0).instanceId();
				return response;
			}

			// Create instance failed. Sleep for a random time and re-try
			int secs = rand.nextInt(START_FAIL_SLEEP_RAND_TIME);
			System.err.println("WARNING: Create instance failed, sleeping " + secs + " seconds before re-trying. tasId: '" + task.getId() + "',  Response: " + response);
			try {
				Thread.sleep(1000 * secs);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		// Too many attempts
		throw new RuntimeException("Unable to create instance for taskId: '" + task.getId() + "' after " + START_FAIL_MAX_ATTEMPTS + "  attempts.");
	}

}
