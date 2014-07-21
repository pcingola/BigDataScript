package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.io.File;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

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

}
