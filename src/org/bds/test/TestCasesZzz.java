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

	//	/**
	//	 * Execute a task having an assignment sys instead of a bare sys
	//	 * 	task(...) {
	//	 * 		out = sys cat z.txt
	//	 * 	}
	//	 */
	//	@Test
	//	public void test04() {
	//		Gpr.debug("Test");
	//		String stdout = runAndCheckStdout("test/run_task_improper_04.bds", "Task improper: End, a=42, str='A=42'");
	//		Assert.assertTrue("Should finish with a 'Done' message", stdout.endsWith("Done\n"));
	//	}

	/**
	 * Test checkpoint during execution of two dependent tasks.
	 * Must recover both tasks and their dependencies
	 */
	@Test
	public void test04() {
		Gpr.debug("Test");
		verbose = true;
		runAndCheckpoint("test/graph_04.bds", "test/graph_04.chp", "out", "IN\nTASK 1\nTASK 2\n");
	}

}
