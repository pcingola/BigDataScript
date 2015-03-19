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
	public void test128_task_local_variables() {
		Gpr.debug("Test");
		runAndCheckStdout("test/run_128.bds", "TEST\n");
	}

}
