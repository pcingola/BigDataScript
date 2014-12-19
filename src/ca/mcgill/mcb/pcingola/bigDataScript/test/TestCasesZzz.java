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
	public void test119_dependency_using_path() {
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

		System.out.println("First run:");
		runAndCheckStdout("test/run_119.bds", expectedStdout1);

		System.out.println("\n\nSecond run:");
		runAndCheckStdout("test/run_119.bds", expectedStdout2);
	}
}
