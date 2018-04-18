package org.bds.test;

import java.util.HashMap;

import org.bds.util.Gpr;
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
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("a", 1);
		expectedValues.put("b", 3);
		expectedValues.put("c", 5);

		runAndCheck("test/run_101.bds", expectedValues);
	}

	//	@Test
	//	public void test102() {
	//		Gpr.debug("Test");
	//		HashMap<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("a", 1);
	//		expectedValues.put("b", 3);
	//		expectedValues.put("c", 5);
	//		expectedValues.put("d", 0);
	//
	//		runAndCheck("test/run_102.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void test103() {
	//		Gpr.debug("Test");
	//		HashMap<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("is", "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]");
	//		expectedValues.put("is2", "[1, 3, 5, 7, 9]");
	//		expectedValues.put("rs", "[1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0]");
	//
	//		runAndCheck("test/run_103.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void test107() {
	//		Gpr.debug("Test");
	//		HashMap<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("paramName", "parameter_value");
	//		expectedValues.put("file1", "/path/to/file_1.txt");
	//		expectedValues.put("file2", "/path/to/file_2.txt");
	//		expectedValues.put("file3", "/path/to/file_3.txt");
	//		expectedValues.put("file4", "/path/to/file_4.txt");
	//		expectedValues.put("file5", "/path/to/file_5.txt");
	//
	//		runAndCheck("test/run_107.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void test108() {
	//		Gpr.debug("Test");
	//		HashMap<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("paramName", "parameter_value");
	//		expectedValues.put("file1", "/path/to/file_1.txt");
	//		expectedValues.put("file2", "/path/to/file_2.txt");
	//		expectedValues.put("file3", "/path/to/file_3.NEW.txt");
	//		expectedValues.put("file4", "/path/to/file_4.txt");
	//		expectedValues.put("file5", "/path/to/file_5.NEW.txt");
	//
	//		runAndCheck("test/run_108.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void test113_parallel_function_calls() {
	//		Gpr.debug("Test");
	//		String stdout = runAndReturnStdout("test/run_113.bds");
	//
	//		Set<String> linesPar = new HashSet<>();
	//		for (String line : stdout.split("\n")) {
	//			if (line.startsWith("Par:")) {
	//				if (linesPar.contains(line)) throw new RuntimeException("Line repeated (this should never happen): '" + line + "'");
	//				linesPar.add(line);
	//			}
	//		}
	//	}
	//
	//	@Test
	//	public void test114_parallel_function_task_calls() {
	//		Gpr.debug("Test");
	//		String stdout = runAndReturnStdout("test/run_114.bds");
	//
	//		Set<String> linesPar = new HashSet<>();
	//		for (String line : stdout.split("\n"))
	//			if (line.startsWith("TASK")) linesPar.add(line);
	//
	//		// Check
	//		Assert.assertTrue("There should be 5 tasks", linesPar.size() == 5);
	//	}
	//
	//	@Test
	//	public void test115_task_dependency_using_taskId() {
	//		Gpr.debug("Test");
	//		String stdout = runAndReturnStdout("test/run_115.bds");
	//		Assert.assertEquals("Hi 1\nBye 1\nHi 2\nBye 2\n", stdout);
	//	}
	//
	//	@Test
	//	public void test116_lineWrap_backslashId() {
	//		Gpr.debug("Test");
	//		String stdout = runAndReturnStdout("test/run_116.bds");
	//		Assert.assertEquals("hi bye\nThe answer\t\tis: 42", stdout);
	//	}
	//
	//	@Test
	//	public void test117_serial_parallel_tasks() {
	//		Gpr.debug("Test");
	//		String expectedStdout = "Iter 1, Task 1: End\n" //
	//				+ "Iter 1, Task 2: Start\n" //
	//				+ "Iter 1, Task 2: End\n" //
	//				+ "Iter 5, Task 1: End\n" //
	//				+ "Iter 5, Task 2: Start\n" //
	//				+ "Iter 5, Task 2: End\n" //
	//		;;
	//
	//		String stdout = runAndReturnStdout("test/run_117.bds");
	//
	//		if (stdout.indexOf(expectedStdout) < 0) {
	//			String msg = "Cannot find desired output:\n" //
	//					+ "---------- Expected output ----------\n" //
	//					+ expectedStdout //
	//					+ "-------------- STDOUT --------------\n" //
	//					+ stdout //
	//			;
	//			System.err.println(msg);
	//			throw new RuntimeException(msg);
	//		}
	//	}
	//
	//	@Test
	//	public void test118_dependency_using_path() {
	//		Gpr.debug("Test");
	//		runAndCheckExit("test/run_118.bds", 0);
	//	}

}
