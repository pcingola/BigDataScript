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
	public void test104() {
		Gpr.debug("Test");
		runAndCheck("test/run_104.bds", "isRun", "true");
	}

	//	@Test
	//	public void test105() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_105.bds", "isRun", "false");
	//	}
	//
	//	@Test
	//	public void test215() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_215.bds", "z", "{ i: 42, j: 7 }");
	//	}
	//
	//	@Test
	//	public void test216() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_216.bds", "z", "{ i: 21, j: 17, next: { i: 42, next: null } }");
	//	}
	//
	//	@Test
	//	public void test217() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_217.bds", "x", "43");
	//	}
	//
	//	@Test
	//	public void test218() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_218.bds", "x", "50");
	//	}

}
