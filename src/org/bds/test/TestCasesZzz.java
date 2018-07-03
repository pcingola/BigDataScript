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

	@Test
	public void test167_binary_expression_assign_bool() {
		Gpr.debug("Test");
		verbose = true;
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("band1", "true");
		expectedValues.put("band2", "false");
		expectedValues.put("bor1", "true");
		expectedValues.put("bor2", "false");
		runAndCheck("test/run_167.bds", expectedValues);
	}

}
