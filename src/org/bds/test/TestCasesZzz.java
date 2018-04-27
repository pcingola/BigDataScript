package org.bds.test;

import java.util.HashMap;

import org.bds.util.Gpr;
import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test50() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("i", "32");
		expectedValues.put("j", "302");
		expectedValues.put("jx", "44");
		expectedValues.put("jy", "91");

		runAndCheck("test/run_50.bds", expectedValues);
	}

	//	@Test
	//	public void testTestCases1() {
	//		Gpr.debug("Test");
	//		runTestCasesPass("test/test_case_run_01.bds");
	//	}
	//
	//	@Test
	//	public void test01_log() {
	//		Gpr.debug("Test");
	//
	//		// Create command line
	//		String args[] = { "-log" };
	//		BdsTest bdsTest = new BdsTest("test/cmdLineOptions_01.bds", args, verbose, debug);
	//
	//		// Run script
	//		bdsTest.run();
	//		bdsTest.checkRunOk();
	//
	//		// Get thread
	//		Bds bds = bdsTest.bds;
	//		BdsThread bdsThread = bds.getBdsRun().getBdsThread();
	//
	//		// Check that all 'log' files exists
	//		String base = bdsThread.getBdsThreadId() + "/task.cmdLineOptions_01.line_3.id_1";
	//
	//		if (verbose) Gpr.debug("Thread ID:" + bdsThread.getBdsThreadId() + "\tBase: " + base);
	//		String exts[] = { "sh", "exitCode", "stderr", "stdout" };
	//		for (String ext : exts) {
	//			String fileName = base + "." + ext;
	//			Assert.assertTrue("Log file '" + fileName + "' not found", Gpr.exists(fileName));
	//		}
	//	}
	//
	//	@Test
	//	public void test01_log_TestCasesClusterGeneric() {
	//		Gpr.debug("Test");
	//
	//		// Create command line
	//		BdsTest bdsTest = new BdsTest("test/clusterGeneric_01.bds", verbose, debug);
	//		bdsTest.bds(false); // Create command now so we can change 'config' before running
	//
	//		// Config generic cluster's scripts
	//		Bds bds = bdsTest.bds;
	//		Config config = bds.getConfig();
	//		config.set(Config.CLUSTER_GENERIC_RUN, "clusterGeneric_localhost/run.pl");
	//		config.set(Config.CLUSTER_GENERIC_KILL, "clusterGeneric_localhost/kill.pl");
	//		config.set(Config.CLUSTER_GENERIC_STAT, "clusterGeneric_localhost/stat.pl");
	//		config.set(Config.CLUSTER_GENERIC_POSTMORTEMINFO, "clusterGeneric_localhost/postMortemInfo.pl");
	//
	//		// Run script
	//		bdsTest.run();
	//		bdsTest.checkRunOk(); // Finished OK?
	//
	//		// Get tasks and check that PID matches 'CLUSTERGENERIC_LOCALHOST_'
	//		// (run.pl prepends that string to PID)
	//		for (Task t : bds.getBdsRun().getBdsThread().getTasks()) {
	//			if (debug) Gpr.debug("Task " + t.getId() + ", pid " + t.getPid());
	//			Assert.assertTrue("Task " + t.getId() + " was NOT executed by ClusterGeneric_localhos (pid " + t.getPid() + ")", t.getPid().startsWith("CLUSTERGENERIC_LOCALHOST_"));
	//		}
	//	}

}
