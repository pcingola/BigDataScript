package org.bds.test.integration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.bds.test.TestCasesBase;
import org.bds.util.Gpr;
import org.bds.util.Timer;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Test cases that require BDS code execution and check results
 *
 * Note: These test cases requires that the BDS code is correctly parsed, compiled and executes.
 *
 * @author pcingola
 *
 */
public class TestCasesIntegrationRun extends TestCasesBase {

	@Test
	public void test104() {
		Gpr.debug("Test");
		runAndCheck("test/run_104.bds", "isRun", "true");
	}

	@Test
	public void test105() {
		Gpr.debug("Test");
		runAndCheck("test/run_105.bds", "isRun", "false");
	}

	@Test
	public void test114_parallel_function_task_calls() {
		Gpr.debug("Test");
		String stdout = runAndReturnStdout("test/run_114.bds");

		Set<String> linesPar = new HashSet<>();
		for (String line : stdout.split("\n"))
			if (line.startsWith("TASK")) linesPar.add(line);

		// Check
		Assert.assertTrue("There should be 5 tasks", linesPar.size() == 5);
	}

	@Test
	public void test115_task_dependency_using_taskId() {
		Gpr.debug("Test");
		String stdout = runAndReturnStdout("test/run_115.bds");
		Assert.assertEquals("Hi 1\nBye 1\nHi 2\nBye 2\n", stdout);
	}

	@Test
	public void test117_serial_parallel_tasks() {
		Gpr.debug("Test");
		String expectedStdout = "Iter 1, Task 1: End\n" //
				+ "Iter 1, Task 2: Start\n" //
				+ "Iter 1, Task 2: End\n" //
				+ "Iter 5, Task 1: End\n" //
				+ "Iter 5, Task 2: Start\n" //
				+ "Iter 5, Task 2: End\n" //
		;;

		String stdout = runAndReturnStdout("test/run_117.bds");

		if (stdout.indexOf(expectedStdout) < 0) {
			String msg = "Cannot find desired output:\n" //
					+ "---------- Expected output ----------\n" //
					+ expectedStdout //
					+ "-------------- STDOUT --------------\n" //
					+ stdout //
			;
			System.err.println(msg);
			throw new RuntimeException(msg);
		}
	}

	@Test
	public void test119_task_dependency() {
		Gpr.debug("Test");

		// Delete input file
		String inFile = "tmp_in.txt";
		(new File(inFile)).delete();

		String expectedStdout1 = "Creating tmp_in.txt\n" //
				+ "Running task\n" //
				+ "Creating tmp_out.txt\n" //
				+ "Done\n" //"
		;

		String expectedStdout2 = "Running task\n" //
				+ "Done\n" //"
		;

		if (verbose) System.out.println("First run:");
		runAndCheckStdout("test/run_119.bds", expectedStdout1);

		if (verbose) System.out.println("\n\nSecond run:");
		runAndCheckStdout("test/run_119.bds", expectedStdout2);
	}

	@Test
	public void test123_literals_task() {
		Gpr.debug("Test");

		String output = "" //
				// Note: This result may change if we use a different taskShell in bds.config
				+ "task               |\t|\n" //
				+ "task               |\t|    variable:Hello\n" //
				+ "task               |\\t|   variable:Hello\n" //
		;

		runAndCheckStdout("test/run_123_literals_task.bds", output);
	}

	@Test
	public void test27() {
		Gpr.debug("Test");
		Timer timer = new Timer();
		timer.start();
		runAndCheck(1, "test/run_27.bds", "timeout", "1"); // 2 seconds timeout
		Assert.assertTrue(timer.elapsed() < 3 * 1000); // We should finish in less than 3 secs (the program waits 60secs)
	}

	@Test
	public void test30() {
		Gpr.debug("Test");
		runAndCheck("test/run_30.bds", "events", "[runnning, wait, done]");
	}

	@Test
	public void test36() {
		Gpr.debug("Test");
		runAndCheck(1, "test/run_36.bds", "s", "before");
	}

	@Test
	public void test84() {
		Gpr.debug("Test");
		runAndCheck(1, "test/run_84.bds", "taskOk", "false");
	}

	@Test
	public void test85() {
		Gpr.debug("Test");
		runAndCheckStderr("test/run_84.bds", "ERROR_TIMEOUT");
	}

	@Test
	public void test87() {
		Gpr.debug("Test");
		runAndCheck("test/run_87.bds", "cpus", "1");
	}

	@Test
	public void test90() {
		Gpr.debug("Test");
		runAndCheck("test/run_90.bds", "ok", "true");
	}

	@Test
	public void test91() {
		Gpr.debug("Test");
		runAndCheck(1, "test/run_91.bds", "ok", "false");
	}

	@Test
	public void test92() {
		Gpr.debug("Test");
		runAndCheck("test/run_92.bds", "outs", "TASK 1\nTASK 2\n");
	}

	@Test
	public void test93() {
		Gpr.debug("Test");
		runAndCheck("test/run_93.bds", "outs", "TASK 1\nTASK 2\n");
	}

}
