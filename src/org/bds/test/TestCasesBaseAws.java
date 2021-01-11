package org.bds.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.bds.BdsLog;
import org.bds.cluster.host.TaskResourcesAws;
import org.bds.data.Data;
import org.bds.data.DataRemote;
import org.bds.data.DataS3;
import org.bds.executioner.ExecutionerCloudAws;
import org.bds.executioner.Executioners;
import org.bds.task.Task;
import org.bds.util.Gpr;
import org.bds.util.GprAws;

import junit.framework.Assert;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.InstanceStateName;
import software.amazon.awssdk.services.ec2.model.Reservation;

/**
 * All methods shared amongst test cases
 *
 * @author pcingola
 */
public class TestCasesBaseAws extends TestCasesBase implements BdsLog {

	// Read bucket name from $HOME/.bds/aws_test_bucket.config
	protected String awsBucketName() {
		return awsTestConfig().get("bucket").trim();
	}

	// Read region from $HOME/.bds/aws_test_bucket.config
	protected String awsRegion() {
		return awsTestConfig().get("region").trim();
	}

	// Read bucket name from $HOME/.bds/aws_test_bucket.config
	protected Map<String, String> awsTestConfig() {
		Map<String, String> keyValues = new HashMap<>();
		String awsBucketNameFile = Gpr.HOME + "/.bds/aws_test.config";
		for (String line : Gpr.readFile(awsBucketNameFile).split("\n")) {
			String[] kv = line.split("\\t");
			keyValues.put(kv[0], kv[1]);
		}
		return keyValues;
	}

	protected String bucketUrl(String dirName, String fileName) {
		String region = awsRegion();
		if (region.isEmpty()) return bucketUrlS3(dirName, fileName);
		else return bucketUrlHttp(dirName, fileName);
	}

	protected String bucketUrlHttp(String dirName, String fileName) {
		return "https://" + awsBucketName() + ".s3." + awsRegion() + ".amazonaws.com/tmp/bds/" + dirName + "/" + fileName;
	}

	protected String bucketUrlS3(String dirName, String fileName) {
		return "s3://" + awsBucketName() + "/tmp/bds/" + dirName + "/" + fileName;
	}

	/**
	 * Check contents of an S3 file
	 */
	protected void checkS3File(DataS3 d, String txt) {
		int txtLen = txt.length();

		// Check some features
		Assert.assertEquals(txtLen, d.size());
		Assert.assertTrue("File '" + d + "' is not a file", d.isFile());
		Assert.assertFalse("File '" + d + "' is a directory?", d.isDirectory());

		// Download file
		boolean ok = d.download();
		Assert.assertTrue("Cannot download file '" + d + "'", ok);
		Assert.assertTrue("Could not download file '" + d + "'", d.isDownloaded());

		// Check download length
		File file = new File(d.getLocalPath());
		Assert.assertEquals("File length does not match expected size (" + txtLen + " != " + file.length() + ")", txtLen, file.length());

		// Check contents
		String txtDownloaded = Gpr.readFile(d.getLocalPath());
		Assert.assertEquals("File '" + d + "' does not match expected content", txt, txtDownloaded);
	}

	/**
	 * Check a file in an S3 bucket
	 */
	protected void checkS3File(String url, String region, String bucket, String path, String paren, String txt) {
		int txtLen = txt.length();
		long now = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();

		DataS3 d = new DataS3(url, region);

		d.setVerbose(verbose);
		d.setDebug(debug);
		long lastMod = d.getLastModified().getTime();
		log("Path: " + d.getPath() + "\tlastModified: " + lastMod + "\tSize: " + d.size());

		// Check some features
		Assert.assertTrue("Is S3?", d instanceof DataS3);
		Assert.assertEquals(path, d.getAbsolutePath());
		Assert.assertEquals(txtLen, d.size());
		Assert.assertTrue((now - lastMod) < 2 * DataRemote.CACHE_TIMEOUT);
		Assert.assertTrue("Is file?", d.isFile());
		Assert.assertFalse("Is directory?", d.isDirectory());

		// Download file
		boolean ok = d.download();
		Assert.assertTrue("Download OK", ok);
		Assert.assertTrue("Is downloaded?", d.isDownloaded());

		// Is it at the correct local file?
		Assert.assertEquals("/tmp/bds/s3/" + bucket.replace('-', '_') + path, d.getLocalPath());
		Assert.assertEquals(path, d.getAbsolutePath());
		Assert.assertEquals(paren, d.getParent().toString());
		Assert.assertEquals("hello.txt", d.getName());

		// Check last modified time
		File file = new File(d.getLocalPath());
		long lastModLoc = file.lastModified();
		Assert.assertTrue("Last modified check:" //
				+ "\n\tlastMod    : " + lastMod //
				+ "\n\tlastModLoc : " + lastModLoc //
				+ "\n\tDiff       : " + (lastMod - lastModLoc)//
				, Math.abs(lastMod - lastModLoc) < 2 * DataRemote.CACHE_TIMEOUT);

		Assert.assertEquals(txtLen, file.length());
		String txtDownloaded = Gpr.readFile(d.getLocalPath());
		Assert.assertEquals(txt, txtDownloaded);
	}

	// Create a file in S3
	protected void createS3File(String s3file, String region, String text) {
		String localFile = "createS3.tmp";
		Gpr.toFile(localFile, text);
		DataS3 ds3 = new DataS3(s3file, region);
		Data dlocal = Data.factory(localFile);
		ds3.upload(dlocal);
		dlocal.delete();
	}

	/**
	 * Find AWS EC2 instance parameters for a given task
	 * @return DescribeInstancesResponse or null if not found
	 */
	protected DescribeInstancesResponse findAwsInstance(Task task) {
		// Find instance
		TaskResourcesAws resources = (TaskResourcesAws) task.getResources();

		// Filter specific instances
		String instanceId = task.getPid();
		if (instanceId == null || instanceId.isEmpty()) return null;

		// Query AWS
		debug("Creating 'DescribeInstancesRequest' request for instance Id '" + instanceId + "', task '" + task.getId() + "'");
		Set<String> instanceIds = new HashSet<>();
		instanceIds.add(instanceId);
		Ec2Client ec2 = GprAws.ec2Client(resources.getRegion());
		DescribeInstancesRequest request = DescribeInstancesRequest.builder().instanceIds(instanceIds).build();
		DescribeInstancesResponse response = ec2.describeInstances(request);
		debug("AWS Instances: " + response);

		return response;
	}

	/**
	 * Make sure there one (and only one) task in ExecutionerCloudAws
	 * Return the task or throw an error if not found
	 */
	protected Task findOneTaskAws() {
		ExecutionerCloudAws excloud = getExecutionerCloudAws();
		List<String> tids = getDoneTaskIdsAws();
		assertTrue("There should be one task, found " + tids.size() + ", task Ids: " + tids, tids.size() == 1);

		// Find task ID
		String tid = tids.get(0);
		Task task = excloud.findTask(tid);
		assertNotNull("Could not find task '" + tid + "'", task);
		return task;
	}

	/**
	 * Get list of task IDs executed on AWS
	 */
	protected List<String> getDoneTaskIdsAws() {
		ExecutionerCloudAws excloud = getExecutionerCloudAws();
		return excloud != null ? excloud.getTaskIdsDone() : new ArrayList<String>();
	}

	/**
	 * Get AWS executioner
	 */
	protected ExecutionerCloudAws getExecutionerCloudAws() {
		// Note that we need to call `getRaw()` because the executioner is invalid after
		// `bds` command finished executing, so the `get()` method would create a new executioner
		return (ExecutionerCloudAws) Executioners.getInstance().getRaw(Executioners.ExecutionerType.AWS);
	}

	/**
	 * Find the first instance state in an AWS EC2 'DescribeInstancesResponse'
	 * @param response
	 * @return The first instance state, throw an error if nothing is found
	 */
	protected InstanceStateName instanceStateName(DescribeInstancesResponse response, String instanceId) {
		for (Reservation reservation : response.reservations()) {
			for (Instance instance : reservation.instances()) {
				debug("Found instance Id '" + instance.instanceId() + "'");
				if (instance.instanceId().equals(instanceId)) {
					InstanceStateName s = instance.state().name();
					log("Instance '" + instanceId + "' has state '" + s + "'");
					return s;
				}
			}
		}
		throw new RuntimeException("Could not find any instance ID '" + instanceId + "' in state in response: " + response);
	}

	/**
	 * Wait for a single AWS task to finish
	 * Check that there is one (and only one) tasks
	 */
	protected void waitAwsTask() {
		int maxIterations = 120;

		// Wait until the instance is created
		Task task = findOneTaskAws();
		String tid = task.getId();
		waitInstanceCreate(task);
		String instanceId = task.getPid();
		debug("Task Id '" + tid + "', has AWS EC2 instance ID '" + instanceId + "'");

		// Wait until the instance terminates
		for (int i = 0; i < maxIterations; i++) {
			// Find instance
			DescribeInstancesResponse response = findAwsInstance(task);
			InstanceStateName state = instanceStateName(response, instanceId);

			debug("Task Id '" + tid + "', has AWS EC2 instance ID '" + instanceId + "', state '" + state + "'");
			if (state == InstanceStateName.TERMINATED) {
				log("Task Id '" + tid + "', has AWS EC2 instance ID '" + instanceId + "', state '" + state + "'");
				return; // OK, we finished successfully
			} else if (state == InstanceStateName.PENDING //
					|| state == InstanceStateName.RUNNING //
					|| state == InstanceStateName.SHUTTING_DOWN //
			) {
				// OK, wait until the instance terminates
			} else {
				throw new RuntimeException("Illegal state: Instance is in an illegal state. Task ID '', instance ID '" + instanceId + "', state '" + state + "'");
			}

			sleep(10); // Sleep for a few seconds
		}

		throw new RuntimeException("Timeout: Too many iterations waiting for instance to finish. Task ID '', instance ID '" + instanceId + "'");
	}

	/**
	 * Wait for an instance to be created
	 * Note: This is based on the fact that the task's PID has the instance ID after is instace for executing a task is created
	 * Waits for 'maxIterations' seconds, checking every second, if not ready thow an error
	 */
	protected void waitInstanceCreate(Task task) {
		int maxIterations = 60;

		for (int i = 0; i < maxIterations; i++) {
			String instanceId = task.getPid();
			if (instanceId != null && !instanceId.isEmpty()) {
				log("Found an instance '" + instanceId + "', for task Id '" + task.getId() + "'");
				return;
			}
			debug("Waiting for an instance, for task Id '" + task.getId() + "'");
			sleep(1);
		}

		throw new RuntimeException("Timeout waiting for an instance: Too many iterations waiting for cloud instance for task '" + task.getId() + "'");
	}

}
