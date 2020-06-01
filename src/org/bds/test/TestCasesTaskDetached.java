package org.bds.test;

import org.bds.util.Gpr;
import org.junit.Test;

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
		runAndCheck("test/run_task_detached_01.bds", "a", "42");
	}

	/**
	 * Execute a detached task: Main bds thread finish before task finishes
	 */
	@Test
	public void test02() {
		Gpr.debug("Test");
		runAndCheck("test/run_task_detached_02.bds", "a", "42");
	}

	/**
	 * Execute two detached task with input dependencies (taskId)
	 */
	@Test
	public void test03() {
		Gpr.debug("Test");
		runAndCheck("test/run_task_detached_03.bds", "a", "42");
	}

	/**
	 * Execute two detached task with output dependencies (files)
	 */
	@Test
	public void test04() {
		Gpr.debug("Test");
		runAndCheck("test/run_task_detached_04.bds", "a", "42");
	}
}
