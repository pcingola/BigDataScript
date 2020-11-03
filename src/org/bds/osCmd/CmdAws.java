package org.bds.osCmd;

import org.bds.cluster.host.TaskResourcesAws;
import org.bds.task.Task;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateTagsRequest;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.RunInstancesResponse;
import software.amazon.awssdk.services.ec2.model.StartInstancesRequest;
import software.amazon.awssdk.services.ec2.model.StartInstancesResponse;
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

	protected Task task;
	protected String instanceId;
	protected String queueName;

	public CmdAws(Task task, String queueName) {
		super(task.getId(), null);
		this.task = task;
	}

	/**
	 * Create an AWS EC2 instance
	 */
	protected String createEC2Instance(Ec2Client ec2) {
		TaskResourcesAws resources = (TaskResourcesAws) task.getResources();

		RunInstancesRequest runRequest = resources.ec2InstanceRequest();

		// Request instance
		RunInstancesResponse response = ec2.runInstances(runRequest);

		response.hasInstances();
		String instanceId = response.instances().get(0).instanceId();

		Tag tag = Tag.builder().key("Name").value(name).build();

		CreateTagsRequest tagRequest = CreateTagsRequest.builder().resources(instanceId).tags(tag).build();

		try {
			ec2.createTags(tagRequest);
			System.out.printf("Successfully started EC2 instance %s based on AMI %s", instanceId, amiId);

			return instanceId;

		} catch (Ec2Exception e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}

		return "";
	}

	@Override
	protected void execCmd() throws Exception {
		// Wait for the process to finish and store exit value

		// exitValue = process.waitFor();
	}

	@Override
	protected boolean execPrepare() throws Exception {
		// TODO: Create the script
		// TODO: Create startup script (task's sys commands or checkpoint)
		// TODO: Find instance's parameters (type, image, disk, etc.)

		// Build instance
		Ec2Client ec2 = Ec2Client.create();
		instanceId = createEC2Instance(ec2);

		// Run instance
		StartInstancesRequest request = StartInstancesRequest.builder().instanceIds(instanceId).build();
		StartInstancesResponse res = ec2.startInstances(request);

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

}
