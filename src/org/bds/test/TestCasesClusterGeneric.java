package org.bds.test;

import org.bds.Bds;
import org.bds.Config;
import org.bds.task.Task;
import org.bds.util.Gpr;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesClusterGeneric extends TestCasesBase {

	@Test
	public void test01_log() {
		Gpr.debug("Test");
		verbose = true;

		// Create command line
		BdsTest bdsTest = new BdsTest("test/clusterGeneric_01.bds", verbose, debug);
		bdsTest.bds(); // Create command now so we can change 'config' before running

		// Config generic cluster's scripts
		Bds bds = bdsTest.bds;
		Config config = bds.getConfig();
		config.set(Config.CLUSTER_GENERIC_RUN, "clusterGeneric_localhost/run.pl");
		config.set(Config.CLUSTER_GENERIC_KILL, "clusterGeneric_localhost/kill.pl");
		config.set(Config.CLUSTER_GENERIC_STAT, "clusterGeneric_localhost/stat.pl");
		config.set(Config.CLUSTER_GENERIC_POSTMORTEMINFO, "clusterGeneric_localhost/postMortemInfo.pl");

		// Run script
		bdsTest.run();
		bdsTest.checkRunOk(); // Finished OK?

		// Get tasks and check that PID matches 'CLUSTERGENERIC_LOCALHOST_'
		// (run.pl prepends that string to PID)
		for (Task t : bds.getBigDataScriptThread().getTasks()) {
			if (debug) Gpr.debug("Task " + t.getId() + ", pid " + t.getPid());
			Assert.assertTrue("Task " + t.getId() + " was NOT executed by ClusterGeneric_localhos (pid " + t.getPid() + ")", t.getPid().startsWith("CLUSTERGENERIC_LOCALHOST_"));
		}
	}
}
