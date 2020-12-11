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

	// Create a remote checkpoint
	@Test
	public void test29() {
		Gpr.debug("Test");
		runAndCheck("test/checkpoint_29.bds", "ok", true);
	}

}
