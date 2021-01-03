package org.bds.test.integration;

import static org.junit.Assert.assertTrue;

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
	 * TODO: AWS task with dependencies (proper and improper task dependent on each other)
	 */
	@Test
	public void test03() {
		Gpr.debug("Test");
		throw new RuntimeException("UNIMPLEMENTED!");
	}

	/**
	 * TODO: Improper task with dependencies
	 */
	@Test
	public void test04() {
		Gpr.debug("Test");
		throw new RuntimeException("UNIMPLEMENTED!");
	}

	/**
	 * TODO: 'dep' and 'goal'
	 */
	@Test
	public void test05() {
		Gpr.debug("Test");
		throw new RuntimeException("UNIMPLEMENTED!");
	}

	/**
	 * TODO: Execute a detached task
	 */
	@Test
	public void test06() {
		Gpr.debug("Test");
		throw new RuntimeException("UNIMPLEMENTED!");
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
