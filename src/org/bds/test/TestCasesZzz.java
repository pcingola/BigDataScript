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
	public void test42() {
		Gpr.debug("Test");
		compileOk("test/test42.bds");
	}

	@Test
	public void test47() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test47.bds', line 3 ] :	Duplicate local name 'gsea' (function 'gsea' declared in test/test47.bds, line 5)";
		compileErrors("test/test47.bds", errs);
	}

	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// FUTURE TEST CASES
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	//	@Test
	//	public void test200() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_200.bds", "", "");
	//	}
	//
	//	@Test
	//	public void test201() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_201.bds", "", "");
	//	}
	//
	//	@Test
	//	public void test202() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_202.bds", "", "");
	//	}
	//
	//	@Test
	//	public void test203() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_203.bds", "", "");
	//	}
	//
	//	@Test
	//	public void test204() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_204.bds", "", "");
	//	}

}
