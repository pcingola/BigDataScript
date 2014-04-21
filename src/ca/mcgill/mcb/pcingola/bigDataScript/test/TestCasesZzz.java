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
	public void test97() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("a", "2");
		expectedValues.put("b", "9");
		expectedValues.put("c", "2");
		expectedValues.put("d", "9");
		runAndCheckMultiple("test/run_97.bds", expectedValues);
	}

}
