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
	public void test136() {
		verbose = true;
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("l", "[1, 99, 2, 3]");
		expectedValues.put("l2", "[3, 2, 99, 1]");
		expectedValues.put("l3", "[3, 2, 99, 1, 99]");
		expectedValues.put("l3count", "2");
		expectedValues.put("l3idx", "2");
		expectedValues.put("l4", "[3, 2, 1, 99]");
		expectedValues.put("l5", "[3, 2, 1, 99]");

		runAndCheck("test/run_136.bds", expectedValues);
	}

}
