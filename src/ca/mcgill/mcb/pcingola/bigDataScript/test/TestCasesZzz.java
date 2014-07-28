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
	public void test32() {
		runAndCheck("test/run_32.bds", "out", "Hi\n");
	}

	@Test
	public void test33() {
		runAndCheck("test/run_33.bds", "err", "Hi\n");
	}

	@Test
	public void test34() {
		runAndCheck("test/run_34.bds", "exitStat", "0");
	}

}
