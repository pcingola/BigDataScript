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

		// Run pipeline again
		System.out.println("Run second time:");
		out = runAndCheckStdout(prefix + ".bds", "copying to csv\ncopying to xml");
		System.out.println(out);
	}

}
