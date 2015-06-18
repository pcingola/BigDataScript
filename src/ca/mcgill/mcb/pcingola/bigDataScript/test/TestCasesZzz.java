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
	public void test137() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("l", "[1, 2, 3]");
		expectedValues.put("has2", "true");
		expectedValues.put("has7", "false");

		runAndCheckMultiple("test/run_137.bds", expectedValues);
	}
}
