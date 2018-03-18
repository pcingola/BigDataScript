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

	@Test
	public void zrun_test15_3() {
		Gpr.debug("Test");
		runAndCheck("test/run_15.bds", "li3", "[apple, orange, 1]");
	}

	//	@Test
	//	public void zrun_test95() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_95.bds", "ll", "[zero, one, two, three, four, 5]");
	//	}
	//
	//	@Test
	//	public void zrun_test116_lineWrap_backslashId() {
	//		Gpr.debug("Test");
	//		String stdout = runAndReturnStdout("test/run_116.bds");
	//		Assert.assertEquals("hi bye\nThe answer\t\tis: 42", stdout);
	//	}

	//
	//	@Test
	//	public void test200() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_200.bds", "", "");
	//	}

}
