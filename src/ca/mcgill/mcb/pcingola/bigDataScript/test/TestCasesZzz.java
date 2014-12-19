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
	public void test35() {
		runAndCheck("test/run_35.bds", "exitStat", "1");
	}

	public void test45() {
		runAndCheckExit("test/run_45.bds", 1);
	}
}
