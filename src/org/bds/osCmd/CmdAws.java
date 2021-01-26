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
import org.bds.run.BdsThread;
import org.bds.run.BdsThreads;
import org.bds.task.Task;
import org.bds.util.Gpr;
import org.bds.util.GprAws;

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

	protected String checkpointS3;
	protected String instanceId;
	protected String queueName;
	protected String startupScriptFileName;
	protected Task task;
	protected String userData;

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
			debug("Created tags for AWS EC2 instance '" + instanceId + "', tags: " + tags);
		} catch (Ec2Exception e) {
			String msg = e.awsErrorDetails().errorMessage();
			error(msg);
			throw new RuntimeException(msg);
		}
	}

	/**
	 * Create an AWS EC2 instance request
	 */
	protected RunInstancesRequest createEc2InstanceRequest(TaskResourcesAws resources) {
		RunInstancesRequest.Builder runRequestBuilder = resources.ec2InstanceRequest(); // Create instance request
		runRequestBuilder.userData(userData);
		return runRequestBuilder.build();
	}

	/**
	 * Create a startup script and encode it as base64
	 */
	String createStartupScript() {
		// Add startup script (encoded as bas64)
		String script = startupScript();
		startupScriptFileName = Gpr.removeExt(task.getProgramFileName()) + ".startup_script.sh";
		if (!Config.get().isLog()) (new File(startupScriptFileName)).deleteOnExit();

		// Write script to local file (logging)
		debug("Writing startup script to '" + startupScriptFileName + "'");
		Gpr.toFile(startupScriptFileName, script);
		File f = new File(startupScriptFileName);
		f.setExecutable(true);

		// Encode to send in AWS request
		String script64 = new String(Base64.getEncoder().encode(script.getBytes()));
		return script64;
	}

	@Override
	protected void execCmd() throws Exception {
		// Nothing to do, we wait for the instance to finish using QueueThreadAwsSqs
	}

	@Override
	protected boolean execPrepare() {
		// If this is an improper task, we need to upload the checkpoint to S3
		if (task.isImproper()) uploadCheckpointToS3();

		// Run an instance (attempts many times before failing)
		return runInstanceLoop();
	}

	@Override
	protected void killCmd() {
		debug("Terminating instance '" + instanceId + "'");
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
	 */
	protected String requestInstance(Ec2Client ec2, RunInstancesRequest runRequest) {
		// Run the instance
		RunInstancesResponse response;
		try {
			response = ec2.runInstances(runRequest);
			debug("Requesting EC2 instange, got response: " + response);
		} catch (SdkClientException e) {
			throw new RuntimeException("EC2 client exception (this might be caused by incorrectly setting the region). " + e.getMessage(), e);
		}

		// Success?
		if (response.hasInstances()) {
			String instanceId = response.instances().get(0).instanceId();
			debug("Created EC2 instance '" + instanceId + "', response: " + response);

			// Save response to file
			String responseFileName = Gpr.removeExt(task.getProgramFileName()) + ".ec2_request_response." + instanceId + ".txt";
			debug("Saving EC2 response to file '" + responseFileName + "'");
			Gpr.toFile(responseFileName, response);
			if (!Config.get().isLog()) (new File(responseFileName)).deleteOnExit();

			return instanceId;
		}

		error("Create instance failed, tasId: '" + task.getId() + "',  Response: " + response);
		return null;
	}

	/**
	 * Run an EC2 instance
	 * @return true on success
	 */
	boolean runInstance() {
		TaskResourcesAws resources = (TaskResourcesAws) task.getResources();// Create and run instance
		if (userData == null) userData = createStartupScript();// Create startup script & userData
		RunInstancesRequest ec2req = createEc2InstanceRequest(resources);// Create instance request

		debug("Creating EC2 instance, request '" + ec2req + "', for task '" + task.getId() + "'");
		Ec2Client ec2 = GprAws.ec2Client(resources.getRegion()); // Create EC2 client

		if (DO_NOT_RUN_INSTANCE) {
			instanceId = "DO_NOT_RUN_INSTANCE";
			task.setPid(instanceId);
		} else {
			instanceId = requestInstance(ec2, ec2req); // Request the instance
			if (instanceId == null) return false; // Failed request?
			task.setPid(instanceId);
			addTags(ec2, resources); // Add tags
		}
		log("Created EC2 instance: '" + instanceId + "', for task '" + task.getId() + "'");
		return true;
	}

	/**
	 * Attempt to run an EC2 instance START_FAIL_MAX_ATTEMPTS sleeping
	 * a random START_FAIL_SLEEP_RAND_TIME number of seconds between
	 * each attempt
	 *
	 * @return true on success
	 */
	protected boolean runInstanceLoop() {
		Random rand = new Random();
		for (int i = 0; i < START_FAIL_MAX_ATTEMPTS; i++) {
			boolean ok = runInstance();

			// Success?
			if (ok) return true;

			// Create instance failed. Sleep for a random time and re-try
			int secs = rand.nextInt(START_FAIL_SLEEP_RAND_TIME);
			warning("Create instance failed, tasId: '" + task.getId() + "',  waiting " + secs + " seconds before next attempt");

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
		sb.append("export HOME='/root'\n"); // This script is executed on startup, there is no HOME directory defined at this stage
		sb.append("trap exit_script EXIT\n"); // Capture exit errors and make sure the instance is terminated
		sb.append("echo \"INFO: Starting script '$0'\"\n\n");
		sb.append(startupScriptMain()); // Startup script "main" part is different for 'sys' or "improper task"
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

		// Timeout
		TaskResourcesAws resources = (TaskResourcesAws) task.getResources();
		long timeout = Math.max(0L, resources.getTimeout());

		// Add bds command to execute the 'dstScriptFile' sending stdout, stderr, and exitCode to SQS
		String stdout = taskDir + "/" + base + ".stdout";
		String stderr = taskDir + "/" + base + ".stderr";
		String exitFile = taskDir + "/" + base + ".exit";
		sb.append("bds exec " //
				+ "-stdout '" + stdout + "' " //
				+ "-stderr '" + stderr + "' " //
				+ "-exit '" + exitFile + "' " //
				+ "-taskId '" + task.getId() + "' " //
				+ "-awsSqsName '" + queueName + "' " // Note that this accepts both queue name and queue URL
				+ "-timeout '" + timeout + "' " //
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

		return sb.toString();
	}

	/**
	 * This command only created the instance
	 * So, in this case, when the command finishes execution, it
	 * only means that the task is going to be executed in an instance
	 * that has just been started.
	 */
	@Override
	protected void stateDone() {
		notifyRunning();
	}

	/**
	 * Change state after executing command
	 */
	@Override
	protected void stateRunningAfter() {
		notifyStarted();
	}

	/**
	 * Change state before executing command
	 */
	@Override
	protected void stateRunningBefore() {
		// Nothing to do
	}

	@Override
	protected void stateStarted() {
		// Nothing to do
	}

	/**
	 * Upload local checkpoint file to S3
	 */
	void uploadCheckpointToS3() {
		TaskResourcesAws resources = (TaskResourcesAws) task.getResources();
		String s3tmp = resources.getS3tmp();
		String bucket = resources.getBucket();
		if (s3tmp == null || s3tmp.isEmpty()) {
			// Try setting a default using S3BUCKET
			if (bucket == null || bucket.isEmpty()) resources.missingValue("s3tmp", TaskResourcesAws.S3_TMP);
			s3tmp = "s3://" + bucket + "/tmp/bds/" + id;
			log("Task resource parameter 's3tmp' not found, creating from parameter 'bucket' ('" + bucket + "'), s3tmp='" + s3tmp + "'");
		}

		checkpointS3 = s3tmp + "/" + task.getId() + ".chp";

		// Local and remote data files
		DataS3 chps3 = new DataS3(checkpointS3, resources.getRegion());
		DataFile localCheckpointFile = new DataFile(task.getCheckpointLocalFile());

		if (task.getCheckpointLocalFile() == null) throw new RuntimeException("AWS improper task '" + task.getId() + "' has null checkpoint file. This should never happen!");
		if (!Gpr.exists(task.getCheckpointLocalFile())) throw new RuntimeException("Checkpoint file '" + task.getCheckpointLocalFile() + "' for AWS improper task '" + task.getId() + "' not found. This should never happen!");

		// Upload checkpoint
		debug("Uploading local checkpoint '" + task.getCheckpointLocalFile() + "' to S3 '" + checkpointS3 + "'");
		chps3.upload(localCheckpointFile);

		// Delete checkpoint once the session finishes
		if (!Config.get().isLog()) {
			BdsThread bdsThread = BdsThreads.getInstance().getOrRoot();
			if (bdsThread != null) {
				bdsThread.rmOnExit(localCheckpointFile);
				bdsThread.rmOnExit(chps3);
			}
		}
	}

}
