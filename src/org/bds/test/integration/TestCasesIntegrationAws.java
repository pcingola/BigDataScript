package org.bds.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.bds.Config;
import org.bds.data.DataS3;
import org.bds.run.BdsRun;
import org.bds.test.TestCasesBaseAws;
import org.bds.util.Gpr;
import org.bds.util.Timer;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases Classes / Objects
 *
 * @author pcingola
 *
 */
public class TestCasesIntegrationAws extends TestCasesBaseAws {

	@Before
	public void beforeEachTest() {
		BdsRun.reset();
		Config.get().load();
	}

	/**
	 * Simple task command
	 */
	@Test
	public void test01_SimpleScript() {
		Gpr.debug("Test");

		String name = "run_aws_01";
		verbose = true;

		// Set the output file
		String region = awsRegion();
		String url = bucketUrl(name, "out.txt");

		// Cleanup: Make sure output file is deleted before starting
		DataS3 dout = new DataS3(url, region);
		dout.delete();

		// Run script
		runOk("test/" + name + ".bds");

		// Check: Check that the output file exists
		assertTrue("Output file '" + dout + "' does not exists", dout.exists());

		// Cleanup
		dout.delete();
	}

	/**
	 * Improper task
	 */
	@Test
	public void test02_ImproperTask() {
		Gpr.debug("Test");

		verbose = true;

		// Set the output file
		String name = "run_aws_02";
		String script = "test/" + name + ".bds";
		String outFile = "out.txt";
		String urlS3 = bucketUrlS3(name, outFile);
		String url = bucketUrl(name, outFile);

		// Expected output:
		StringBuilder sb = new StringBuilder();
		sb.append("Before task\n");
		sb.append("Output file: '" + urlS3 + "'\n");
		sb.append("After task\n");
		sb.append("Start: Improper task on AWS\n");
		sb.append("Uploading local file 'tmp.txt' to '" + urlS3 + "'\n");
		for (int i = 0; i < 10; i++)
			sb.append("Counting: " + i + "\nThis is an echo " + i + "\n");
		sb.append("End: Improper task on AWS\n");
		sb.append("Done\n");
		String expectedStdout = sb.toString();

		// Cleanup: Make sure output file is deleted before starting
		String region = awsRegion();
		DataS3 dout = new DataS3(url, region);
		dout.delete();

		// Run script and check that 'expectedStdout' is in STDOUT
		runAndCheckStdout(script, expectedStdout);

		// Check: Check that the output file exists
		assertTrue("Output file '" + dout + "' does not exists", dout.exists());

		// Cleanup
		dout.delete();

		// Check that there was only one task executed on AWS (for this test program)
		findOneTaskAws();
	}

	/**
	 * AWS task with dependencies (proper and improper task dependent on each other)
	 */
	@Test
	public void test03_DependentTasks() {
		Gpr.debug("Test");

		verbose = true;

		// Set the output file
		String name = "run_aws_03";
		String script = "test/" + name + ".bds";
		String inFile = "in.txt";
		String out1File = "out1.txt";
		String out2File = "out2.txt";

		// URLs in 's3://' style
		String urlInS3 = bucketUrlS3(name, inFile);
		String urlOut1S3 = bucketUrlS3(name, out1File);
		String urlOut2S3 = bucketUrlS3(name, out2File);

		// URLs (could be in 'https://' format if the region i sexplicitly defined)
		String urlIn = bucketUrl(name, inFile);
		String urlOut1 = bucketUrl(name, out1File);
		String urlOut2 = bucketUrl(name, out2File);

		Random rand = new Random();
		String inTxt = String.format("RAND_%x", rand.nextLong());
		log("Input text: " + inTxt);

		// Expected output:
		StringBuilder sb = new StringBuilder();
		// Main script (local)
		sb.append("Files:\n");
		sb.append("\tin: '" + urlInS3 + "'\n");
		sb.append("\tout1: '" + urlOut1S3 + "'\n");
		sb.append("\tout2: '" + urlOut2S3 + "'\n");
		sb.append("Before task1\n");
		sb.append("After task1\n");
		sb.append("Before task2\n");
		sb.append("After task2\n");
		// Task1 (aws, instance 1)
		sb.append("Start: Task1 improper\n");
		sb.append("Input text: '" + inTxt + "'\n");
		sb.append("End: Task1 Improper\n");
		// Task2 (aws, instance 2)
		sb.append("Start: Task2\n");
		sb.append("Input:\n");
		sb.append("OUT1: '" + inTxt + "'\n");
		sb.append("End: Task2\n");
		// Main script (local)
		sb.append("Done\n");
		String expectedStdout = sb.toString();

		// Cleanup: Make sure output file is deleted before starting
		String region = awsRegion();
		DataS3 din = new DataS3(urlIn, region);
		DataS3 dout1 = new DataS3(urlOut1, region);
		DataS3 dout2 = new DataS3(urlOut2, region);
		din.delete();
		dout1.delete();
		dout2.delete();

		// Run script and check that 'expectedStdout' is in STDOUT
		String args[] = { "-inTxt", inTxt };
		runAndCheckStdout(script, expectedStdout, null, args, false);

		// Check: Check that the output file exists
		assertTrue("Input file '" + din + "' does not exists", din.exists());
		assertTrue("Output file '" + dout1 + "' does not exists", dout1.exists());
		assertTrue("Output file '" + dout2 + "' does not exists", dout2.exists());

		// Check: Check file contents
		String dinExpected = inTxt;
		checkS3File(din, dinExpected);

		String dout1Expected = "OUT1: '" + inTxt + "'";
		checkS3File(dout1, dout1Expected);

		String dout2Expected = "OUT2\n" + dout1Expected;
		checkS3File(dout2, dout2Expected);

		// Check that there were two tasks executed by this script on AWS
		List<String> tids = getDoneTaskIdsAws();
		assertEquals("The number of tasks executed on AWS doesn't match the expected, tids (size=" + tids.size() + "): '" + tids + "'", 2, tids.size());

		// Cleanup
		din.delete();
		dout1.delete();
		dout2.delete();
	}

	/**
	 * AWS dependent tasks using: 'dep' and 'goal'
	 */
	@Test
	public void test04_DependentTasksDepGoal() {
		Gpr.debug("Test");

		verbose = true;

		// Set the output file
		String name = "run_aws_04";
		String script = "test/" + name + ".bds";
		String inFile = "in.txt";
		String out1File = "out1.txt";
		String out2File = "out2.txt";

		// URLs in 's3://' style
		String urlInS3 = bucketUrlS3(name, inFile);
		String urlOut1S3 = bucketUrlS3(name, out1File);
		String urlOut2S3 = bucketUrlS3(name, out2File);

		// URLs (could be in 'https://' format if the region i sexplicitly defined)
		String urlIn = bucketUrl(name, inFile);
		String urlOut1 = bucketUrl(name, out1File);
		String urlOut2 = bucketUrl(name, out2File);

		Random rand = new Random();
		String inTxt = String.format("RAND_%x", rand.nextLong());
		log("Input text: " + inTxt);

		// Expected output:
		StringBuilder sb = new StringBuilder();
		// Main script (local)
		sb.append("Files:\n");
		sb.append("\tin: '" + urlInS3 + "'\n");
		sb.append("\tout1: '" + urlOut1S3 + "'\n");
		sb.append("\tout2: '" + urlOut2S3 + "'\n");
		sb.append("Before task1\n");
		sb.append("After task1\n");
		sb.append("Before task2\n");
		sb.append("After task2\n");
		sb.append("Goal: '" + urlOut2S3 + "'\n");
		// Task1 (aws, instance 1)
		sb.append("Start: Dep1 improper\n");
		sb.append("Input text: '" + inTxt + "'\n");
		sb.append("End: Dep1 Improper\n");
		// Task2 (aws, instance 2)
		sb.append("Start: Dep2\n");
		sb.append("Input:\n");
		sb.append("OUT1: '" + inTxt + "'\n");
		sb.append("End: Dep2\n");
		// Main script (local)
		sb.append("Done\n");
		String expectedStdout = sb.toString();

		// Cleanup: Make sure output file is deleted before starting
		String region = awsRegion();
		DataS3 din = new DataS3(urlIn, region);
		DataS3 dout1 = new DataS3(urlOut1, region);
		DataS3 dout2 = new DataS3(urlOut2, region);
		din.delete();
		dout1.delete();
		dout2.delete();

		// Run script and check that 'expectedStdout' is in STDOUT
		String args[] = { "-inTxt", inTxt };
		runAndCheckStdout(script, expectedStdout, null, args, false);

		// Check: Check that the output file exists
		assertTrue("Input file '" + din + "' does not exists", din.exists());
		assertTrue("Output file '" + dout1 + "' does not exists", dout1.exists());
		assertTrue("Output file '" + dout2 + "' does not exists", dout2.exists());

		// Check: Check file contents
		String dinExpected = inTxt;
		checkS3File(din, dinExpected);

		String dout1Expected = "OUT1: '" + inTxt + "'";
		checkS3File(dout1, dout1Expected);

		String dout2Expected = "OUT2\n" + dout1Expected;
		checkS3File(dout2, dout2Expected);

		// Check that there were two tasks executed by this script on AWS
		List<String> tids = getDoneTaskIdsAws();
		assertEquals("The number of tasks executed on AWS doesn't match the expected, tids (size=" + tids.size() + "): '" + tids + "'", 2, tids.size());

		// Cleanup
		din.delete();
		dout1.delete();
		dout2.delete();
	}

	/**
	 * Execute a detached task on AWS
	 * WARNIGN: This test might take several minutes to execute and creates an AWS EC2 instance!
	 */
	@Test
	public void test07_DetachedAwsTask() {
		Gpr.debug("Test");

		// Set the output file
		String region = awsRegion();
		String url = bucketUrl("run_aws_07", "out.txt");

		// Cleanup: Make sure output file is deleted before starting
		DataS3 dout = new DataS3(url, region);
		dout.delete();

		// Run: Execute bds code and check how long before it completes
		Timer t = new Timer();
		runOk("test/run_aws_07.bds");

		// Check: A detached task should finish immediately
		//        The original program's task loops for 60 seconds, so a detached task should take less than a minute
		assertTrue("Detached task taking too long: Probably not detached", t.elapsedSecs() < 15);

		verbose = true;

		// Wait: Make sure the task is executed on AWS, check instance and instance states
		waitAwsTask();

		// Check: Check that the output file exists
		assertTrue("Output file '" + dout + "' does not exists", dout.exists());

		// Cleanup
		dout.delete();
	}

}
