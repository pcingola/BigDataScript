package org.bds.test;

import java.io.File;

import org.bds.Config;
import org.bds.util.Gpr;
import org.junit.Before;
import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Before
	public void beforeEachTest() {
		Config.reset();
		Config.get().load();
	}

	@Test
	public void test119_task_dependency() {
		Gpr.debug("Test");
		verbose = true;

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

}
