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
	public void test101() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("a", 1);
		expectedValues.put("b", 3);
		expectedValues.put("c", 5);

		runAndCheckMultiple("test/run_101.bds", expectedValues);
	}

}
