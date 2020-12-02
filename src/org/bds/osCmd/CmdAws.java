package org.bds.osCmd;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bds.Config;
import org.bds.cluster.host.TaskResourcesAws;
import org.bds.data.DataFile;
import org.bds.data.DataS3;
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

	public static boolean DO_NOT_RUN_INSTANCE = false; // This is used for developing or debugging

	// TODO: These parameters should be configurable (maybe in taskResources?)
	public static int START_FAIL_MAX_ATTEMPTS = 50;
	public static int START_FAIL_SLEEP_RAND_TIME = 60;

	protected Task task;
	protected String instanceId;
	protected String queueName;
	protected String checkpointS3;

	public CmdAws(Task task, String queueName) {
		super(task.getId(), null);
		this.task = task;
		this.queueName = queueName;
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
		String script = startupScript();
		String startupScriptFileName = Gpr.removeExt(task.getProgramFileName()) + ".startup_script.sh";
		if (!Config.get().isLog()) (new File(startupScriptFileName)).deleteOnExit();

		// Write script to local file (logging)
		Gpr.debug("Writing startup script to '" + startupScriptFileName + "'");
		Gpr.toFile(startupScriptFileName, script);
		File f = new File(startupScriptFileName);
		f.setExecutable(true);

		// Encode to send in AWS request
		String script64 = new String(Base64.getEncoder().encode(script.getBytes()));
		runRequestBuilder.userData(script64);

		// Run the instance
		RunInstancesRequest runRequest = runRequestBuilder.build();
		if (DO_NOT_RUN_INSTANCE) {
			Gpr.debug("WARNING: DO_NOT_RUN_INSTANCE is activated");
			return "instance_id_123";
		}

		runInstance(ec2, runRequest);
		addTags(ec2, resources); // Add tags

		return instanceId;
	}

	@Override
	protected void execCmd() throws Exception {
		// Nothing to do, we wait for the instance to finish using QueueThreadAwsSqs
	}

	/**
	 * This command only created the instance
	 * So, in this case, when the command finishes execution, it
	 * only means that the task is going to be executed in an instance
	 * that has just been started.
	 */
	@Override
	protected void execDone() {
		stateDone();
		if (notifyTaskState != null) notifyTaskState.taskRunning(task);
	}

	@Override
	protected boolean execPrepare() throws Exception {
		// If this is an improper task, we need to upload the checkpoint to S3
		if (task.isImproper()) uploadCheckpointToS3();

		// Create and run instance
		createEC2Instance();
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
	 * Prepend a "#\t" to each line of the script
	 */
	String prepentHashTab(String systxt) {
		StringBuilder sb = new StringBuilder();
		for (String l : systxt.split("\n"))
			sb.append("#\t" + l + "\n");
		return sb.toString();
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

	/**
	 * Creates the instance's startup script
	 */
	protected String startupScript() {
		StringBuilder sb = new StringBuilder();
		sb.append("#!" + Config.get().getTaskShell() + "\n\n");

		sb.append(startupScriptExit());

		// Script main
		sb.append("trap exit_script EXIT\n");
		sb.append("echo \"INFO: Starting script '$0'\"\n\n");
		sb.append(startupScriptMain());
		sb.append("\necho \"INFO: Finished script '$0'\"\n");
		return sb.toString();
	}

	/**
	 * Script exit function: Shutdown the instance, unless 'keepInstanceAliveAfterFinish' is set
	 */
	protected String startupScriptExit() {
		StringBuilder sb = new StringBuilder();
		TaskResourcesAws resources = (TaskResourcesAws) task.getResources();

		sb.append("function exit_script {\n");

		if (!resources.isKeepInstanceAliveAfterFinish()) sb.append("  shutdown -h now\n");
		else sb.append("  echo 'INFO: Keeping instance alive (keepInstanceAliveAfterFinish was set)'\n");

		sb.append("}\n\n");

		return sb.toString();
	}

	/**
	 * Main part of the startup script
	 * Create a startup script to execute the task
	 * We need to
	 * 	- Create a script file (with either the sys commands or a 'bds -task' for improper task
	 * 	- Execute the previous script using 'bds exec -awsSqsName ...', so all messages get redirected to the SQS queue
	 */
	protected String startupScriptMain() {
		StringBuilder sb = new StringBuilder();
		String base = Gpr.baseName(task.getId());
		String dir = Gpr.dirName(task.getId());
		String taskDir = task.getCurrentDir() + "/" + dir;
		String dstScriptFile = taskDir + "/" + base + ".startup_script_instance.sh";
		sb.append("mkdir -p '" + taskDir + "'\n");

		// Create a script file 'dstScriptFile'
		if (task.isImproper()) {
			// If the task is improper, the script just runs "bds -task"
			sb.append(startupScriptMainImproper(dstScriptFile));
		} else {
			// If the task is "proper", we execute pack the 'sys' commands
			// within this startup script and unpack them to 'dstScriptFile'
			sb.append(startupScriptMainSys(dstScriptFile));
		}

		// Make sure we the script file is executable
		sb.append("chmod u+x '" + dstScriptFile + "'\n");

		// Add bds command to execute the 'dstScriptFile' sending stdout, stderr, and exitCode to SQS
		String stdout = taskDir + "/" + base + ".stdout";
		String stderr = taskDir + "/" + base + ".stderr";
		String exitFile = taskDir + "/" + base + ".exit";
		TaskResourcesAws resources = (TaskResourcesAws) task.getResources();
		sb.append("bds exec " //
				+ "-stdout '" + stdout + "' " //
				+ "-stderr '" + stderr + "' " //
				+ "-exit '" + exitFile + "' " //
				+ "-taskId '" + task.getId() + "' " //
				+ "-awsSqsName '" + queueName + "' " //
				+ "-timeout '" + resources.getTimeout() + "' " //
				+ "'" + dstScriptFile + "'\n" //
		);

		return sb.toString();
	}

	/**
	 * Create a startup script to execute an improper task
	 * We need to execute
	 * 		bds exec ... -awsSqsName ... task_improper.sh
	 *
	 * where that 'task_improper.sh' process simple calls:
	 * 		bds -task s3://path/to/checkpoint.chp
	 *
	 */
	protected String startupScriptMainImproper(String dstScriptFile) {
		StringBuilder sb = new StringBuilder();

		// Create a script to execute 'bds -task ...'
		sb.append("echo '#!/bin/bash' > '" + dstScriptFile + "'\n");
		sb.append("echo 'bds -task \"" + checkpointS3 + "\"' >> '" + dstScriptFile + "'\n");

		return sb.toString();
	}

	/**
	 * Create a startup script to execute 'sys' commands in task (i.e. a "propper" task)
	 */
	protected String startupScriptMainSys(String dstScriptFile) {
		StringBuilder sb = new StringBuilder();

		// In order to "encode" the sys commands in this startup script, we
		// add all the 'sys' commands as a comment (i.e. prepend '#\t' to
		// each line). Then we can extract them by using 'grep'.
		// WARNINGI: We must be careful not to include any '#\t' in the rest
		// of the startup script, otherwise this "encoding" will break.
		sb.append(prepentHashTab(task.getProgramTxtShell()) + "\n");

		// Write commands to extract the 'sys' commands from this script
		// file into 'dstScriptFile'. We need to 'grep' for '#\t' and
		// then extract the first two characters (i.e. '#\t') from each line
		sb.append("grep '^#\t' \"$0\" | cut -c 3- > '" + dstScriptFile + "'\n");

		// Make sure we can execute the script file
		sb.append("chmod u+x '" + dstScriptFile + "'\n");

		return sb.toString();
	}

	@Override
	protected void stateDone() {
		started = true;
		executing = false;
	}

	/**
	 * Upload local checkpoint file to S3
	 */
	void uploadCheckpointToS3() {
		TaskResourcesAws resources = (TaskResourcesAws) task.getResources();
		String s3tmp = resources.getS3tmp();
		if (s3tmp == null || s3tmp.isEmpty()) resources.missingValue("s3tmp", TaskResourcesAws.S3_TMP);

		checkpointS3 = s3tmp + "/" + task.getId() + ".chp";

		// Local and remote data files
		DataS3 chps3 = new DataS3(checkpointS3);
		DataFile chp = new DataFile(task.getCheckpointLocalFile());

		if (task.getCheckpointLocalFile() == null) throw new RuntimeException("AWS improper task '" + task.getId() + "' has null checkpoint file. This should never happen!");
		if (!Gpr.exists(task.getCheckpointLocalFile())) throw new RuntimeException("Checkpoint file '" + task.getCheckpointLocalFile() + "' for AWS improper task '" + task.getId() + "' not found. This should never happen!");

		// Upload checkpoint
		if (debug) Timer.showStdErr("Uploading local checkpoint '" + task.getCheckpointLocalFile() + "' to S3 '" + checkpointS3 + "'");
		chps3.upload(chp);

		// TODO: Delete checkpoint once the session finishes. Use BdsThear.rmOnexit() and bds-exec (go)
	}

}
