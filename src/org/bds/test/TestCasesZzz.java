package org.bds.test;

import org.bds.Config;
import org.bds.run.BdsRun;
import org.bds.util.Gpr;
import org.junit.Before;
import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBaseAws {

	@Before
	public void beforeEachTest() {
		BdsRun.reset();
		Config.get().load();
	}

	@Test
	public void test() {
		Gpr.debug("Test");
		verbose = true;
		runAndCheck("test/zz.bds", "r", "0");
	}

	//	/**
	//	 * Test a simple command running via 'ssh cluster'
	//	 */
	//	@Test
	//	public void test01() {
	//		Gpr.debug("Test");
	//
	//		// Create command line
	//		String[] args = { "-c", "test/clusterSsh_localhost_01.config" };
	//		BdsTest bdsTest = new BdsTest("test/clusterSsh_01.bds", args, verbose, debug);
	//		bdsTest.bds(false);
	//
	//		// Run script
	//		bdsTest.run();
	//		bdsTest.checkRunOk(); // Finished OK?
	//
	//		// Get tasks and check that PID matches 'CLUSTERGENERIC_LOCALHOST_'
	//		// (run.pl prepends that string to PID)
	//		for (Task t : bdsTest.bds.getBdsRun().getBdsThread().getTasks()) {
	//			debug("Task " + t.getId() + ", pid " + t.getPid());
	//			Assert.assertTrue("Task " + t.getId() + " was NOT executed by 'Cluster Ssh', task id " + t.getId() //
	//					, t.getId().toUpperCase().startsWith("CLUSTERSSH") //
	//			);
	//		}
	//	}
	//
	//	/**
	//	 * Improper task
	//	 */
	//	@Test
	//	public void test02_ImproperTask() {
	//		Gpr.debug("Test");
	//
	//		verbose = true;
	//
	//		// Set the output file
	//		String name = "run_aws_02";
	//		String script = "test/" + name + ".bds";
	//		String outFile = "out.txt";
	//		String urlS3 = bucketUrlS3(name, outFile);
	//		String url = bucketUrl(name, outFile);
	//
	//		// Expected output:
	//		StringBuilder sb = new StringBuilder();
	//		sb.append("Before task\n");
	//		sb.append("Output file: '" + urlS3 + "'\n");
	//		sb.append("After task\n");
	//		sb.append("Start: Improper task on AWS\n");
	//		sb.append("Uploading local file 'tmp.txt' to '" + urlS3 + "'\n");
	//		for (int i = 0; i < 10; i++)
	//			sb.append("Counting: " + i + "\nThis is an echo " + i + "\n");
	//		sb.append("End: Improper task on AWS\n");
	//		sb.append("Done\n");
	//		String expectedStdout = sb.toString();
	//
	//		// Cleanup: Make sure output file is deleted before starting
	//		String region = awsRegion();
	//		DataS3 dout = new DataS3(url, region);
	//		dout.delete();
	//
	//		// Run script and check that 'expectedStdout' is in STDOUT
	//		runAndCheckStdout(script, expectedStdout);
	//
	//		// Check: Check that the output file exists
	//		assertTrue("Output file '" + dout + "' does not exists", dout.exists());
	//
	//		// Cleanup
	//		dout.delete();
	//
	//		// Check that there was only one task executed on AWS (for this test program)
	//		findOneTaskAws();
	//	}
	//
	//	/**
	//	 * Execute two detached task + one taks
	//	 */
	//	@Test
	//	public void test03_TwoDetachedOneDependent() {
	//		Gpr.debug("Test");
	//		String outFile = "tmp.run_task_detached_03.txt";
	//
	//		String catout = "Task 1: Start\n" + //
	//				"Task 2: Start\n" + //
	//				"Task 3\n";
	//
	//		String outAfterWaitExpected = "Task 1: Start\n" + //
	//				"Task 2: Start\n" + //
	//				"Task 3\n" + //
	//				"Task 1: End\n" + //
	//				"Task 2: End\n";
	//
	//		runAndCheck("test/run_task_detached_03.bds", "catout", catout);
	//
	//		sleep(3);
	//
	//		String outAfterWait = Gpr.readFile(outFile);
	//		Assert.assertEquals(outAfterWaitExpected, outAfterWait);
	//	}

}
