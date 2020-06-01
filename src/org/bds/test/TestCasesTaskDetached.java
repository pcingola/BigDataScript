package org.bds.test;

import org.bds.util.Gpr;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Test cases Classes / Objects
 *
 * @author pcingola
 *
 */
public class TestCasesTaskDetached extends TestCasesBase {

	/**
	 * Execute a detached task: Local computer
	 */
	@Test
	public void test01() {
		Gpr.debug("Test");
		runAndCheckStdout("test/run_task_detached_01.bds", "Before\nAfter\nDone\n");
	}

	/**
	 * Execute two detached task with input dependencies (taskId)
	 */
	@Test
	public void test02() {
		Gpr.debug("Test");
		String outFile = "tmp.run_task_detached_02.txt";

		String catout = "Task 1: Start\n" + //
				"Task 2: Start\n" + //
				"Task 2: End\n";

		String outAfterWaitExpected = "Task 1: Start\n" + //
				"Task 2: Start\n" + //
				"Task 2: End\n" + //
				"Task 1: End\n";

		runAndCheck("test/run_task_detached_02.bds", "catout", catout);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String outAfterWait = Gpr.readFile(outFile);
		Assert.assertEquals(outAfterWaitExpected, outAfterWait);
	}

	/**
	 * Execute two detached task + one taks
	 */
	@Test
	public void test03() {
		Gpr.debug("Test");
		String outFile = "tmp.run_task_detached_03.txt";

		String catout = "Task 1: Start\n" + //
				"Task 2: Start\n" + //
				"Task 3\n";

		String outAfterWaitExpected = "Task 1: Start\n" + //
				"Task 2: Start\n" + //
				"Task 3\n" + //
				"Task 1: End\n" + //
				"Task 2: End\n";

		runAndCheck("test/run_task_detached_03.bds", "catout", catout);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String outAfterWait = Gpr.readFile(outFile);
		Assert.assertEquals(outAfterWaitExpected, outAfterWait);
	}

	/**
	 * Execute two detached task with output dependencies (files)
	 */
	@Test
	public void test04() {
		Gpr.debug("Test");
		String expectedExceptionMessage = "Detached task output files cannot be used as dependencies";
		BdsTest bdsTest = runAndCheckExit("test/run_task_detached_04.bds", 1);

		// Check that the exception causing the 'exit=1' code is the one we expected
		Throwable javaException = bdsTest.bds.getBdsRun().getBdsThread().getVm().getJavaException();
		Assert.assertTrue(javaException.getMessage().startsWith(expectedExceptionMessage));
	}
}
