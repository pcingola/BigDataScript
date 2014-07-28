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
	public void test67() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("s", "varS");
		expectedValues.put("s1", "Hi '$'");
		expectedValues.put("s2", "Hi $");
		expectedValues.put("s3", "Hi $ bye");
		runAndCheckMultiple("test/run_67.bds", expectedValues);
	}

}
