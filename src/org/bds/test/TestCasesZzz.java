package org.bds.test;

import org.bds.Config;
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

	//	@Test
	//	public void test250_super_super_method_call() {
	//		verbose = true;
	//		runAndCheckStdout("test/run_250.bds", "GrandParent\nParent\nChild\n");
	//	}
	//

	@Test
	public void test251_super_super_constructor_call() {
		verbose = true;
		debug = true;
		runAndCheckStdout("test/run_251.bds", "GrandParent\nParent\nChild\n");
	}

	//	@Test
	//	public void test252_super_notsuper() {
	//		verbose = true;
	//		debug = true;
	//		runAndCheckStdout("test/run_252.bds", "GrandParent\nParent\nChild\n");
	//	}

}
