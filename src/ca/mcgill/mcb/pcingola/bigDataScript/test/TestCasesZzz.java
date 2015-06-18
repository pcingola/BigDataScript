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

	/**
	 * Show help when there are no arguments
	 */
	@Test
	public void test134_automatic_help_sections() {
		Gpr.debug("Test");
		String output = "This program does blah\n" //
				+ "Actually, a lot of blah blah\n" //
				+ "    and even more blah\n" //
				+ "    or blah\n" //
				+ "\t-quiet <bool>     : Be very quiet\n" //
				+ "\t-verbose <bool>   : Be verbose\n" //
				+ "Options related to database\n" //
				+ "\t-dbName <string>  : Database name\n" //
				+ "\t-dbPort <int>     : Database port\n" //
				+ "\n" //
				;

		// Run and capture stdout
		String args[] = { "test/run_134.bds", "-h" };
		String stdout = runAndReturnStdout(args);
		if (verbose) System.err.println("STDOUT: " + stdout);

		// Check that sys and task outputs are not there
		Assert.assertEquals(output, stdout);
	}

}
