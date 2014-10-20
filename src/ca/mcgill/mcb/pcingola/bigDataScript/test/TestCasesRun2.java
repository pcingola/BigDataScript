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

	@Test
	public void test106() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("str1", "l[1] : '2'");
		expectedValues.put("str2", "m{'Hello'} : 'Bye'");

		runAndCheckMultiple("test/run_106.bds", expectedValues);
	}

	@Test
	public void test107() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("paramName", "parameter_value");
		expectedValues.put("file1", "/path/to/file_1.txt");
		expectedValues.put("file2", "/path/to/file_2.txt");
		expectedValues.put("file3", "/path/to/file_3.txt");
		expectedValues.put("file4", "/path/to/file_4.txt");
		expectedValues.put("file5", "/path/to/file_5.txt");

		runAndCheckMultiple("test/run_107.bds", expectedValues);
	}

	@Test
	public void test108() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("paramName", "parameter_value");
		expectedValues.put("file1", "/path/to/file_1.txt");
		expectedValues.put("file2", "/path/to/file_2.txt");
		expectedValues.put("file3", "/path/to/file_3.NEW.txt");
		expectedValues.put("file4", "/path/to/file_4.txt");
		expectedValues.put("file5", "/path/to/file_5.NEW.txt");

		runAndCheckMultiple("test/run_108.bds", expectedValues);
	}

	public void test109() {
		runAndCheck("test/run_109.bds", "r1", "4027146782649399912");
	}

	public void test110() {
		runAndCheck("test/run_110.bds", "runOk", "true");
	}

	public void test111() {
		runAndCheck("test/run_111.bds", "runOk", "false");
	}

	public void test112() {
		runAndCheck("test/run_112.bds", "runOk", "false");
	}

}
