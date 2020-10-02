package org.bds.test;

import java.util.HashMap;

import org.bds.Config;
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
	public void test254_getvar() {
		HashMap<String, Object> expectedValues = new HashMap<>();

		expectedValues.put("shome", System.getenv().get("HOME"));
		expectedValues.put("szzz", "VALUE_DEFAULT_2");
		expectedValues.put("szzzxxxzzz", "VALUE_DEFAULT_3");

		runAndCheck("test/run_254.bds", expectedValues);
	}

}
