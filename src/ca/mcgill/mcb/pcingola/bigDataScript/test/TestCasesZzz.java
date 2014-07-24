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

	@Test
	public void test36() {
		runAndCheck("test/run_36.bds", "s", "before");
	}

	@Test
	public void test84() {
		runAndCheck("test/run_84.bds", "taskOk", "false");
	}

	@Test
	public void test91() {
		runAndCheck("test/run_91.bds", "ok", "false");
	}

}
