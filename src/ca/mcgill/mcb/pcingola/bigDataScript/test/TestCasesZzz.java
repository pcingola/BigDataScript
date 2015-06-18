package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.util.HashMap;

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
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("l", "[1, 99, 2, 3]");
		expectedValues.put("l2", "[3, 2, 99, 1]");
		expectedValues.put("l3", "[3, 2, 99, 1, 99]");
		expectedValues.put("l3count", "2");
		expectedValues.put("l3idx", "2");
		expectedValues.put("l4", "[3, 2, 1, 99]");
		expectedValues.put("l5", "[3, 2, 1, 99]");

		runAndCheckMultiple("test/run_136.bds", expectedValues);
	}
}
