package org.bds.test;

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
	public void test03() {
		verbose = debug = true;
		Gpr.debug("Test");
		runAndCheckpoint("test/graph_03.bds", "test/graph_03.chp", "out", "Task start\nTask end\n");
	}

	//	@Test
	//	public void test04() {
	//		Gpr.debug("Test");
	//		runAndCheckpoint("test/graph_04.bds", "test/graph_04.chp", "out", "IN\nTASK 1\nTASK 2\n");
	//	}
	//
	//	@Test
	//	public void test06() {
	//		Gpr.debug("Test");
	//		// Remove old entries
	//		String prefix = "test/graph_06";
	//		File txt = new File(prefix + ".txt");
	//		File csv = new File(prefix + ".csv");
	//		File xml = new File(prefix + ".xml");
	//		txt.delete();
	//		csv.delete();
	//		xml.delete();
	//
	//		// Create file
	//		Gpr.toFile(prefix + ".txt", "TEST");
	//
	//		// Run pipeline first
	//		System.out.println("Run first time:");
	//		String out = runAndCheckStdout(prefix + ".bds", "copying to csv\ncopying to xml");
	//		System.out.println(out);
	//
	//		// Remove CSV file
	//		csv.delete();
	//
	//		// Run pipeline again (nothing should happen, since XML is 'up to date' with respect to TXT)
	//		System.out.println("Run second time:");
	//		out = runAndCheckStdout(prefix + ".bds", "copying", true);
	//		System.out.println(out);
	//	}
	//
	//
	//	@Test
	//	public void test10() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/graph_10.bds", "num", "2");
	//	}

}
