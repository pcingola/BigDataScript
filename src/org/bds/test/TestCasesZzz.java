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
	public void test17() {
		Gpr.debug("Test");
		verbose = true;
		compileOk("test/test17.bds");
	}

	//	@Test
	//	public void test18() {
	//		Gpr.debug("Test");
	//		compileOk("test/test18.bds");
	//	}
	//
	//	@Test
	//	public void test20() {
	//		Gpr.debug("Test");
	//		compileOk("test/test20.bds");
	//	}
	//
	//	@Test
	//	public void test21() {
	//		Gpr.debug("Test");
	//		compileOk("test/test21.bds");
	//	}
	//
	//	@Test
	//	public void test22() {
	//		Gpr.debug("Test");
	//		compileOk("test/test22.bds");
	//	}
	//
	//	@Test
	//	public void test23() {
	//		Gpr.debug("Test");
	//		compileOk("test/test23.bds");
	//	}
	//
	//	@Test
	//	public void test30() {
	//		Gpr.debug("Test");
	//		String errs = "ERROR [ file 'test/test30.bds', line 4 ] :	Cannot cast real to int\n";
	//		compileErrors("test/test30.bds", errs);
	//	}
	//
	//	@Test
	//	public void test31() {
	//		Gpr.debug("Test");
	//		String errs = "ERROR [ file 'test/test31.bds', line 4 ] :	Function has no return statement\n";
	//		compileErrors("test/test31.bds", errs);
	//	}
	//
	//	@Test
	//	public void test200() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_200.bds", "", "");
	//	}

}
