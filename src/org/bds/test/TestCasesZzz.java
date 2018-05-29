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
	//	public void test163() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		// Define and create inner classes
	//		runAndCheck("test/run_162.bds", "out", "B: A: Hi");
	//	}

}
