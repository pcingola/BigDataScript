package ca.mcgill.mcb.pcingola.bigDataScript.test;

import junit.framework.Assert;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript;
import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

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

		// Create command line
		BigDataScript bds = bds("test/clusterGeneric_01.bds");

		// Config generic cluster's scripts
		Config config = bds.getConfig();
		config.set(Config.CLUSTER_GENERIC_RUN, "clusterGeneric_localhost/run.pl");
		config.set(Config.CLUSTER_GENERIC_KILL, "clusterGeneric_localhost/kill.pl");
		config.set(Config.CLUSTER_GENERIC_STAT, "clusterGeneric_localhost/stat.pl");
		config.set(Config.CLUSTER_GENERIC_POSTMORTEMINFO, "clusterGeneric_localhost/postMortemInfo.pl");

		// Run script
		int exitCode = bds.run();

		// Finished OK?
		Assert.assertEquals(0, exitCode);

		// Get tasks and check that PID matches 'CLUSTERGENERIC_LOCALHOST_'
		// (run.pl prepends that string to PID)
		for (Task t : bds.getBigDataScriptThread().getTasks()) {
			if (debug) Gpr.debug("Task " + t.getId() + ", pid " + t.getPid());
			Assert.assertTrue("Task " + t.getId() + " was NOT executed by ClusterGeneric_localhos (pid " + t.getPid() + ")", t.getPid().startsWith("CLUSTERGENERIC_LOCALHOST_"));
		}
	}
}
