package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.util.HashMap;

import org.junit.Test;

/**
 * Test cases that require BDS code execution and check results
 *
 * Note: These test cases requires that the BDS code is correctly parsed, compiled and executes.
 *
 * @author pcingola
 *
 */
public class TestCasesRun2 extends TestCasesBase {

	@Test
	public void test100() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("s", 1);
		expectedValues.put("s2", -1);

		runAndCheckMultiple("test/run_100.bds", expectedValues);
	}

}
