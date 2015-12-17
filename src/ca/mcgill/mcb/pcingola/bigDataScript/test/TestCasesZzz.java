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
	public void test32() {
		Gpr.debug("PATH: " + System.getenv("PATH"));
		verbose = true;
		Gpr.debug("Test");
		runAndCheck("test/run_32.bds", "out", "Hi\n");
	}

}
