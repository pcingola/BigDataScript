package org.bds.test;

import org.bds.Config;
import org.bds.util.Gpr;
import org.junit.Before;
import org.junit.Test;

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

	@Test
	public void test18_out_tasksId() {
		Gpr.debug("Test");
		runAndCheckStderr("test/graph_18.bds", "Cannot have task as a dependency output");
	}

	//	@Test
	//	public void test19_dep_task_already_executed() {
	//		Gpr.debug("Test");
	//		verbose = debug = true;
	//		runAndCheckStdout("test/graph_19.bds", "Hello\nBye");
	//	}
	//	@Test
	//	public void test20_dep_goal_taskid() {
	//		Gpr.debug("Test");
	//		verbose = debug = true;
	//		runAndCheckStdout("test/graph_20.bds", "Hello\nBye");
	//	}
	//
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
