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
	public void test100() {
		Gpr.debug("Test");
		verbose = debug = true;

		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("s", 1);
		expectedValues.put("s2", -1);

		runAndCheck("test/run_100.bds", expectedValues);
	}

}
