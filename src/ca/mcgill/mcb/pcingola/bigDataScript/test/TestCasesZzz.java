package ca.mcgill.mcb.pcingola.bigDataScript.test;

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
	public void test127_interpolate_variable_with_underscores() {
		Gpr.debug("Test");

		String output = "bwa parameters\n" //
				+ "bwa parameters\n" //
				+ "bwa parameters\n" //
				+ "bwa parameters\n" //
		;

		runAndCheckStdout("test/run_127.bds", output);
	}

}
