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
	//	 * Execute several tasks within a 'par' function
	//	 */
	//	@Test
	//	public void test06() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		List<String> expected = new ArrayList<>();
	//		expected.add("Task improper: Before, a=42");
	//		for (int i = 0; i < 5; i++) {
	//			expected.add("Task improper: Start, a=42, i=" + i);
	//			expected.add("Task improper: End, a=" + (42 + i) + ", i=" + i);
	//		}
	//		expected.add("Task improper: After, a=42");
	//
	//		String stdout = runAndCheckStdout("test/run_task_improper_06.bds", expected, null, false);
	//		Assert.assertTrue("Should finish with a 'Done' message", stdout.endsWith("Done\n"));
	//	}

	@Test
	public void test04() {
		Gpr.debug("Test");
		verbose = true;
		runAndCheckpoint("test/graph_04.bds", "test/graph_04.chp", "out", "IN\nTASK 1\nTASK 2\n");
	}

}
