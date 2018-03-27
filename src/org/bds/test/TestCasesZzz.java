package org.bds.test;

import org.bds.util.Gpr;
import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	//
	//	@Test
	//	public void test200() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_200.bds", "", "");
	//	}

	@Test
	public void test213() {
		Gpr.debug("Test");
		verbose = true;
		runAndCheck("test/run_213.bds", "z", "{ i: 7 }");
	}

}
