package ca.mcgill.mcb.pcingola.bigDataScript.test;

import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test104() {
		runAndCheck("test/run_104.bds", "isRun", "true");
	}

	@Test
	public void test105() {
		runAndCheck("test/run_105.bds", "isRun", "false");
	}
}
