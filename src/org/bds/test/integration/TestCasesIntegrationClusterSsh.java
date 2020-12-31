package org.bds.test.integration;

import org.bds.task.Task;
import org.bds.test.BdsTest;
import org.bds.test.TestCasesBase;
import org.bds.util.Gpr;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesIntegrationClusterSsh extends TestCasesBase {

	/**
	 * Test a simple command running via 'ssh cluster'
	 */
	@Test
	public void test01() {
		Gpr.debug("Test");

		// Create command line
		String[] args = { "-c", "test/clusterSsh_localhost_01.config" };
		BdsTest bdsTest = new BdsTest("test/clusterSsh_01.bds", args, verbose, debug);
		bdsTest.bds(false);

		// Run script
		bdsTest.run();
		bdsTest.checkRunOk(); // Finished OK?

		// Get tasks and check that PID matches 'CLUSTERGENERIC_LOCALHOST_'
		// (run.pl prepends that string to PID)
		for (Task t : bdsTest.bds.getBdsRun().getBdsThread().getTasks()) {
			debug("Task " + t.getId() + ", pid " + t.getPid());
			Assert.assertTrue("Task " + t.getId() + " was NOT executed by 'Cluster Ssh', task id " + t.getId() //
					, t.getId().toUpperCase().startsWith("CLUSTERSSH") //
			);
		}
	}
}
