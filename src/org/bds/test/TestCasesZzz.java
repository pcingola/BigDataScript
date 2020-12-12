package org.bds.test;

import org.bds.Config;
import org.junit.Before;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Before
	public void beforeEachTest() {
		Config.reset();
		Config.get().load();
	}

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
	//			if (debug) Gpr.debug("Task " + t.getId() + ", pid " + t.getPid());
	//			Assert.assertTrue("Task " + t.getId() + " was NOT executed by 'Cluster Ssh', task id " + t.getId() //
	//					, t.getId().toUpperCase().startsWith("CLUSTERSSH") //
	//			);
	//		}
	//	}

}
