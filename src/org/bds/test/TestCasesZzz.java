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

	//	@Test
	//	public void test200() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_200.bds", "", "");
	//	}

	@Test
	public void test210() {
		Gpr.debug("Test");
		runAndCheckStderr("test/run_210.bds", "Null pointer: Cannot call method 'Zzz.set' in null object'");
	}

}
