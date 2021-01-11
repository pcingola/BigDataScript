package org.bds.test.integration;

import java.util.ArrayList;
import java.util.List;

import org.bds.test.TestCasesBase;
import org.bds.util.Gpr;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases Classes / Objects
 *
 * @author pcingola
 *
 */
public class TestCasesIntegrationTaskImproper extends TestCasesBase {

	/**
	 * Execute an improper task: Local computer
	 */
	@Test
	public void test01_SimpleTaskCheckVarianbleInMain() {
		Gpr.debug("Test");
		runAndCheck("test/run_task_improper_01.bds", "a", "42");
	}

	/**
	 * Execute a task: Capture exit command, stdout, stderr
	 */
	@Test
	public void test02_SimpleTaskCheckOutputs() {
		Gpr.debug("Test");
		List<String> expected = new ArrayList<>();
		expected.add("Task improper: Before, a=42");
		expected.add("Task improper: Start, a=42");
		expected.add("Task improper: End, a=1");
		expected.add("Task improper: After, a=42");
		runAndCheckStdout("test/run_task_improper_02.bds", expected, null, false);
	}

	/**
	 * Execute two tasks: Make sure execution ends at task end and environment is inherited
	 */
	@Test
	public void test03_ForLoopMultipleTasks() {
		Gpr.debug("Test");

		List<String> expected = new ArrayList<>();
		expected.add("Task improper: Before, a=42");
		for (int i = 0; i < 3; i++) {
			expected.add("Task improper: Start, a=42, i=" + i);
			expected.add("Task improper: End, a=" + (42 + i) + ", i=" + i);
		}
		expected.add("Task improper: After, a=42");

		String stdout = runAndCheckStdout("test/run_task_improper_03.bds", expected, null, false);
		Assert.assertTrue("Should finish with a 'Done' message", stdout.endsWith("Done\n"));
	}

	/**
	 * Execute a task having an assignment sys instead of a bare sys
	 * 	task(...) {
	 * 		out = sys cat z.txt
	 * 	}
	 */
	@Test
	public void test04_SysInImproperTask() {
		Gpr.debug("Test");
		String stdout = runAndCheckStdout("test/run_task_improper_04.bds", "Task improper: End, a=42, str='A=42'");
		Assert.assertTrue("Should finish with a 'Done' message", stdout.endsWith("Done\n"));
	}

	/**
	 * Execute several tasks within a function
	 */
	@Test
	public void test05_ImproperTaskInFunction() {
		Gpr.debug("Test");
		List<String> expected = new ArrayList<>();
		expected.add("Task improper: Before, a=42");
		for (int i = 0; i < 3; i++) {
			expected.add("Task improper: Start, a=42, i=" + i);
			expected.add("Task improper: End, a=" + (42 + i) + ", i=" + i);
		}
		expected.add("Task improper: After, a=42");

		String stdout = runAndCheckStdout("test/run_task_improper_05.bds", expected, null, false);
		Assert.assertTrue("Should finish with a 'Done' message", stdout.endsWith("Done\n"));
	}

	/**
	 * Execute several tasks within a 'par' function
	 */
	@Test
	public void test06_ImproperTaskInPar() {
		Gpr.debug("Test");
		List<String> expected = new ArrayList<>();
		expected.add("Task improper: Before, a=42");
		for (int i = 0; i < 5; i++) {
			expected.add("Task improper: Start, a=42, i=" + i);
			expected.add("Task improper: End, a=" + (42 + i) + ", i=" + i);
		}
		expected.add("Task improper: After, a=42");

		String stdout = runAndCheckStdout("test/run_task_improper_06.bds", expected, null, false);
		Assert.assertTrue("Should finish with a 'Done' message", stdout.endsWith("Done\n"));
	}

	/**
	 * Execute an improper task that fails
	 */
	@Test
	public void test07_SysErrorInImproperTask() {
		Gpr.debug("Test");
		runAndCheckExit("test/run_task_improper_07.bds", 1);
	}

	/**
	 * 'dep' and 'goal' using improper tasks
	 */
	@Test
	public void test08_DepGoalImproperDependencies() {
		Gpr.debug("Test");

		String stdout = //
				"Task improper: Start\n" //
						+ "DEP_1: Start\n" //
						+ "DEP_1: End\n" //
						+ "DEP_2: Start\n" //
						+ "DEP_2: End\n" //
						+ "Task improper: End\n";

		runAndCheckStdout("test/run_task_improper_08.bds", stdout);
	}

	/**
	 * 'dep' and 'goal' using improper tasks with taskId dependencies
	 */
	@Test
	public void test09_DepGoalImproperDependenciesWithTaskIds() {
		Gpr.debug("Test");

		String stdout = //
				"Task improper: Start\n" //
						+ "DEP_1: Start\n" //
						+ "DEP_1: End\n" //
						+ "DEP_2: Start\n" //
						+ "DEP_2: End\n" //
						+ "DEP_3: Start\n" //
						+ "DEP_3: End\n" //
						+ "Task improper: End\n";

		runAndCheckStdout("test/run_task_improper_09.bds", stdout);
	}

}
