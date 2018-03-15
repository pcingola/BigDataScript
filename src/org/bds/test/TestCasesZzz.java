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

	//	@Test
	//	public void test201() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		Map<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("z", "{ i: 0, r: 0.0, s: \"\" }");
	//		expectedValues.put("z2", "{ i: 0, r: 0.0, s: \"\" }");
	//
	//		runAndCheck("test/run_201.bds", expectedValues);
	//	}

	@Test
	public void test202() {
		Gpr.debug("Test");
		runAndCheck("test/run_202.bds", "z", "{ i: 42, r: 1.234, s: \"Hi\" }");
	}

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
	//
	//	@Test
	//	public void test205() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_205.bds", "", "");
	//	}
	//
	//	@Test
	//	public void test206() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_206.bds", "", "");
	//	}
	//
	//	@Test
	//	public void test207() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_207.bds", "", "");
	//	}

}
