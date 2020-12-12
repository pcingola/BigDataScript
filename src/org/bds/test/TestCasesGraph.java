package org.bds.test;

import java.io.File;

import org.bds.util.Gpr;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Test cases aboug graph construction and 'implicit wait' commands
 *
 * @author pcingola
 *
 */
public class TestCasesGraph extends TestCasesBase {

	@Test
	public void test01() {
		Gpr.debug("Test");
		runAndCheck("test/graph_01.bds", "output", "IN\nTASK 1\nTASK 2\n");
	}

	@Test
	public void test02() {
		Gpr.debug("Test");
		runAndCheck("test/graph_02.bds", "output", "IN\nTASK 1\nTASK 2\nTASK 3\n");
	}

	@Test
	public void test03() {
		Gpr.debug("Test");
		runAndCheckpoint("test/graph_03.bds", "test/graph_03.chp", "out", "Task start\nTask end\n");
	}

	@Test
	public void test04() {
		Gpr.debug("Test");
		runAndCheckpoint("test/graph_04.bds", "test/graph_04.chp", "out", "IN\nTASK 1\nTASK 2\n");
	}

	@Test
	public void test05() {
		Gpr.debug("Test");
		// Remove old entries
		File txt = new File("test/graph_05.txt");
		File csv = new File("test/graph_05.csv");
		File xml = new File("test/graph_05.xml");
		txt.delete();
		csv.delete();
		xml.delete();

		// Create file
		Gpr.toFile("test/graph_05.txt", "TEST");

		// Run pipeline first
		if (verbose) System.out.println("Run first time:");
		String out = runAndCheckStdout("test/graph_05.bds", "copying to csv\ncopying to xml");
		if (verbose) System.out.println(out);

		// Remove CSV file
		csv.delete();

		// Run pipeline again
		if (verbose) System.out.println("Run second time:");
		out = runAndCheckStdout("test/graph_05.bds", "copying to csv\ncopying to xml");
		if (verbose) System.out.println(out);
	}

	@Test
	public void test06() {
		Gpr.debug("Test");
		// Remove old entries
		String prefix = "test/graph_06";
		File txt = new File(prefix + ".txt");
		File csv = new File(prefix + ".csv");
		File xml = new File(prefix + ".xml");
		txt.delete();
		csv.delete();
		xml.delete();

		// Create file
		Gpr.toFile(prefix + ".txt", "TEST");

		// Run pipeline first
		if (verbose) System.out.println("Run first time:");
		String out = runAndCheckStdout(prefix + ".bds", "copying to csv\ncopying to xml");
		if (verbose) System.out.println(out);

		// Remove CSV file
		csv.delete();

		// Run pipeline again (nothing should happen, since XML is 'up to date' with respect to TXT)
		if (verbose) System.out.println("Run second time:");
		out = runAndCheckStdout(prefix + ".bds", "copying", true);
		if (verbose) System.out.println(out);
	}

	@Test
	public void test08() {
		Gpr.debug("Test");
		runAndCheckStdout("test/graph_08.bds", "MID1\nMID2\nOUT");
	}

	@Test
	public void test09() {
		Gpr.debug("Test");
		runAndCheckStderr("test/graph_09.bds", "Circular dependency");
	}

	@Test
	public void test10() {
		Gpr.debug("Test");
		runAndCheck("test/graph_10.bds", "num", "2");
	}

	@Test
	public void test11_circularDependencyCheck() {
		Gpr.debug("Test");
		runAndCheck("test/graph_11.bds", "ok", "true");
	}

	@Test
	public void test12_circularDependency() {
		Gpr.debug("Test");
		runAndCheckStderr("test/graph_12.bds", "Circular dependency");
	}

	@Test
	public void test13_goal_using_taskId() {
		Gpr.debug("Test");
		runAndCheckStdout("test/graph_13.bds", "out1_2.txt\nout2_1.txt");
	}

	@Test
	public void test14_dep_using_taskId() {
		Gpr.debug("Test");
		runAndCheckStdout("test/graph_14.bds", "Hello\nBye");
	}

	@Test
	public void test15_goal_using_list() {
		Gpr.debug("Test");
		String out = runAndReturnStdout("test/graph_15.bds");
		Assert.assertTrue(out.contains("Hi 0\n"));
		Assert.assertTrue(out.contains("Hi 1\n"));
		Assert.assertTrue(out.contains("Hi 2\n"));
	}

	@Test
	public void test16_dep_no_input() {
		Gpr.debug("Test");
		runAndCheckStdout("test/graph_16.bds", "DEP_1\nDEP_2\nDEP_3");
	}

	@Test
	public void test17_circular_taskid() {
		Gpr.debug("Test");
		runAndCheckStderr("test/graph_17.bds", "Circular dependency on task");
	}

	@Test
	public void test18_out_tasksId() {
		Gpr.debug("Test");
		runAndCheckStderr("test/graph_18.bds", "Cannot have task as a dependency output");
	}

}
