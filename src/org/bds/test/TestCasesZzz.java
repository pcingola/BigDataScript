package org.bds.test;

import org.bds.util.Gpr;
import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test93() {
		Gpr.debug("Test");
		runAndCheck("test/run_93.bds", "outs", "TASK 1\nTASK 2\n");
	}

}
