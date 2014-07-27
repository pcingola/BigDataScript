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

	@Test
	public void test101() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("a", 1);
		expectedValues.put("b", 3);
		expectedValues.put("c", 5);

		runAndCheckMultiple("test/run_101.bds", expectedValues);
	}

	@Test
	public void test102() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("a", 1);
		expectedValues.put("b", 3);
		expectedValues.put("c", 5);
		expectedValues.put("d", 0);

		runAndCheckMultiple("test/run_102.bds", expectedValues);
	}

	@Test
	public void test103() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("is", "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]");
		expectedValues.put("is2", "[1, 3, 5, 7, 9]");
		expectedValues.put("rs", "[1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0]");

		runAndCheckMultiple("test/run_103.bds", expectedValues);
	}

	@Test
	public void test104() {
		runAndCheck("test/run_104.bds", "isRun", "true");
	}

	@Test
	public void test105() {
		runAndCheck("test/run_105.bds", "isRun", "false");
	}

}
