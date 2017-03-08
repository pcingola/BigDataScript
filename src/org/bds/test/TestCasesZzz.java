package org.bds.test;

import java.util.HashMap;

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
	public void test149_div() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("val0", 50);
		expectedValues.put("val1", 50.0);
		expectedValues.put("val2", 50.0);
		expectedValues.put("val3", 50.0);

		runAndCheck("test/run_149.bds", expectedValues);
	}

	@Test
	public void test150_mult() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("val0", 200);
		expectedValues.put("val1", 200.0);
		expectedValues.put("val2", 200.0);
		expectedValues.put("val3", 200.0);

		runAndCheck("test/run_150.bds", expectedValues);
	}

	@Test
	public void test151_plus() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("val0", 102);
		expectedValues.put("val1", 102.0);
		expectedValues.put("val2", 102.0);
		expectedValues.put("val3", 102.0);

		runAndCheck("test/run_151.bds", expectedValues);
	}

	@Test
	public void test152_minus() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("val0", 98);
		expectedValues.put("val1", 98.0);
		expectedValues.put("val2", 98.0);
		expectedValues.put("val3", 98.0);

		runAndCheck("test/run_152.bds", expectedValues);
	}

}
