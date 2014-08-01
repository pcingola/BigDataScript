package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.io.File;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Test cases aboug graph construction and 'implicit wait' commands
 *
 * @author pcingola
 *
 */
public class TestCasesGraph extends TestCasesBase {

	@Test
	public void test01() {
		runAndCheck("test/graph_01.bds", "output", "IN\nTASK 1\nTASK 2\n");
	}

	@Test
	public void test02() {
		runAndCheck("test/graph_02.bds", "output", "IN\nTASK 1\nTASK 2\nTASK 3\n");
	}

	@Test
	public void test03() {
		runAndCheckpoint("test/graph_03.bds", "test/graph_03.chp", "out", "Task start\nTask end\n");
	}

	@Test
	public void test04() {
		runAndCheckpoint("test/graph_04.bds", "test/graph_04.chp", "out", "IN\nTASK 1\nTASK 2\n");
	}

	@Test
	public void test05() {
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
		System.out.println("Run first time:");
		String out = runAndCheckStdout("test/graph_05.bds", "copying to csv\ncopying to xml");
		System.out.println(out);

		// Remove CSV file
		csv.delete();

		// Run pipeline again
		System.out.println("Run second time:");
		out = runAndCheckStdout("test/graph_05.bds", "copying to csv\ncopying to xml");
		System.out.println(out);
	}

	@Test
	public void test06() {
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
		System.out.println("Run first time:");
		String out = runAndCheckStdout(prefix + ".bds", "copying to csv\ncopying to xml");
		System.out.println(out);

		// Remove CSV file
		csv.delete();

		// Run pipeline again (nothing should happen, since XML is 'up to date' with respect to TXT)
		System.out.println("Run second time:");
		out = runAndCheckStdout(prefix + ".bds", "copying", true);
		System.out.println(out);
	}

	@Test
	public void test08() {
		runAndCheckStdout("test/graph_08.bds", "MID1\nMID2\nOUT");
	}

	@Test
	public void test09() {
		runAndCheckStderr("test/graph_09.bds", "Circular dependency");
	}

}
