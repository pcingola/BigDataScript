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

	//	@Test
	//	public void test47() {
	//		Gpr.debug("Test");
	//		String errs = "ERROR [ file 'test/test47.bds', line 3 ] :	Duplicate local name 'gsea' (function 'gsea' declared in test/test47.bds, line 5)";
	//		compileErrors("test/test47.bds", errs);
	//	}
	//
	//	@Test
	//	public void test50() {
	//		Gpr.debug("Test");
	//		String errs = "ERROR [ file 'test/test50.bds', line 6 ] :\tCannot assign to non-variable 'f(  )[0]'";
	//		compileErrors("test/test50.bds", errs);
	//	}
	//
	//	@Test
	//	public void test51() {
	//		Gpr.debug("Test");
	//		String errs = "ERROR [ file 'test/test51.bds', line 6 ] :	Cannot assign to non-variable 'f(  ){\"hi\"}'";
	//		compileErrors("test/test51.bds", errs);
	//	}
	//
	//	@Test
	//	public void zrun_test50() {
	//		Gpr.debug("Test");
	//		HashMap<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("i", "32");
	//		expectedValues.put("j", "302");
	//		expectedValues.put("jx", "44");
	//		expectedValues.put("jy", "91");
	//
	//		runAndCheck("test/run_50.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void zrun_test15_3() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_15.bds", "li3", "[apple, orange, 1]");
	//	}
	//
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
	//	public void zrun_test140_list_nonvariable() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_140.bds", "i", "2");
	//	}
	//
	//	@Test
	//	public void zrun_test141_map_nonvariable() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_141.bds", "i", "42");
	//	}
	//
	//	@Test
	//	public void test200() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_200.bds", "", "");
	//	}

}
