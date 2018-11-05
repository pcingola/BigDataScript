package org.bds.test;

import java.util.HashMap;
import java.util.Map;

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

	//	@Test
	//	public void test230_tryCatch() {
	//		Gpr.debug("Test");
	//		Map<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("try1", "true");
	//		expectedValues.put("catch1", "false");
	//		expectedValues.put("finally1", "true");
	//
	//		runAndCheck("test/run_230.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void test231_tryCatch() {
	//		Gpr.debug("Test");
	//		Map<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("try1", "true");
	//		expectedValues.put("catch1", "true");
	//		expectedValues.put("finally1", "true");
	//
	//		runAndCheck("test/run_231.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void test232_tryCatch() {
	//		Gpr.debug("Test");
	//		Map<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("f1", "true");
	//		expectedValues.put("f2", "false");
	//		expectedValues.put("try1", "true");
	//		expectedValues.put("try2", "false");
	//		expectedValues.put("catch1", "true");
	//		expectedValues.put("finally1", "true");
	//
	//		runAndCheck("test/run_232.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void test233_tryCatch() {
	//		Gpr.debug("Test");
	//		Map<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("f1", "true");
	//		expectedValues.put("f2", "true");
	//		expectedValues.put("try1", "true");
	//		expectedValues.put("try2", "false");
	//		expectedValues.put("catch1", "true");
	//		expectedValues.put("finally1", "true");
	//
	//		runAndCheck("test/run_233.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void test234_tryCatch() {
	//		Gpr.debug("Test");
	//		Map<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("try11", "true");
	//		expectedValues.put("try12", "false");
	//		expectedValues.put("catch11", "true");
	//		expectedValues.put("finally11", "true");
	//
	//		expectedValues.put("try21", "true");
	//		expectedValues.put("try22", "true");
	//		expectedValues.put("catch21", "false");
	//		expectedValues.put("finally21", "true");
	//
	//		runAndCheck("test/run_234.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void test235_tryCatch() {
	//		Gpr.debug("Test");
	//		Map<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("f11", "true");
	//		expectedValues.put("f12", "false");
	//		expectedValues.put("f21", "true");
	//		expectedValues.put("f22", "false");
	//
	//		expectedValues.put("try11", "true");
	//		expectedValues.put("try12", "false");
	//		expectedValues.put("catch11", "false");
	//		expectedValues.put("finally11", "true");
	//
	//		expectedValues.put("try21", "true");
	//		expectedValues.put("try22", "false");
	//		expectedValues.put("catch21", "true");
	//		expectedValues.put("finally21", "true");
	//
	//		runAndCheck("test/run_235.bds", expectedValues);
	//	}

	@Test
	public void test235_tryCatch() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("f11", "true");
		expectedValues.put("f12", "false");
		expectedValues.put("f21", "true");
		expectedValues.put("f22", "false");

		expectedValues.put("try11", "true");
		expectedValues.put("try12", "false");
		expectedValues.put("catch11", "false");
		expectedValues.put("finally11", "true");

		expectedValues.put("try21", "true");
		expectedValues.put("try22", "false");
		expectedValues.put("catch21", "true");
		expectedValues.put("finally21", "true");

		runAndCheck("test/run_235.bds", expectedValues);
	}

}
