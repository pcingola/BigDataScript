package ca.mcgill.mcb.pcingola.bigDataScript.test;

import junit.framework.Assert;

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
	public void test125_automatic_help() {
		Gpr.debug("Test");
		String output = "Command line options 'run_125.bds' :\n" //
				+ "\t-num <int>                             : Number of times 'hi' should be printed\n" //
				+ "\t-min <int>                             : Help for argument 'min' should be printed here\n" //
				+ "\t-mean <int>                            : Help for argument 'mean' should be printed here\n" //
				+ "\t-someVeryLongCommandLineArgumentName   : This command line argument has a really long name\n" //
				+ "\t-salutation <string>                   : Salutation to use\n" //
				+ "\t-useTab                                : Use tab before printing line\n" //
				+ "\n" //
		;

		// Run and capture stdout
		String args[] = { "test/run_125.bds", "-h" };
		String stdout = runAndReturnStdout(args);
		if (verbose) System.err.println("STDOUT: " + stdout);

		// Check that sys and task outputs are not there
		Assert.assertEquals(output, stdout);
	}

}
