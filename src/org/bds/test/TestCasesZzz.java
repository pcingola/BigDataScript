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
	public void test210() {
		Gpr.debug("Test");
		verbose = true;
		runAndCheckStderr("test/run_210.bds", "Null pointer: Cannot call method 'Zzz.set' in null object");
	}

	//	@Test
	//	public void test211() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_211.bds", "z", "{ i: 7 }");
	//	}
	//
	//	@Test
	//	public void test212() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_212.bds", "z", "{ i: 42 }");
	//	}
	//
	//	@Test
	//	public void test213() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_213.bds", "z", "{ i: 7 }");
	//	}
	//
	//	@Test
	//	public void test214() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_214.bds", "z", "{ i: 42 }");
	//	}

}
