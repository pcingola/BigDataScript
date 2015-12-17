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
	public void test123_literals_sys_task() {
		verbose = true;
		Gpr.debug("Test");

		String output = "print_quote        |\\t|\n" //
				+ "print_quote        |\\t|    variable:$hi\n" //
				+ "print_double       |\t|\n" //
				+ "print_double       |\t|    variable:Hello\n" //
				+ "print_double_esc   |\\t|\n" //
				+ "print_double_esc   |\\t|   variable:Hello\n" //
		// Note: This result may change if we use a different sysShell in bds.config
				+ "sys                |\\t|\n" //
				+ "sys                |\\t|    variable:Hello\n" //
		// Note: This result may change if we use a different taskShell in bds.config
				+ "task               |\\t|\n" //
				+ "task               |\\t|    variable:Hello\n" //
				;

		runAndCheckStdout("test/run_123.bds", output);
	}

}
