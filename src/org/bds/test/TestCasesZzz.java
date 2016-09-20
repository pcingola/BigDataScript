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
	public void test85() {
		Gpr.debug("Test");
		verbose = true;
		runAndCheckStderr("test/run_84.bds", "ERROR_TIMEOUT");
	}

}
