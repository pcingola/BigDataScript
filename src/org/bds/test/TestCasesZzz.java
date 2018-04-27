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
	public void test02_help() {
		Gpr.debug("Test");

		verbose = true;

		// Create command line
		String args[] = { "-h" };
		BdsTest bdsTest = new BdsTest("test/cmdLineOptions_02.bds", args, verbose, debug);

		bdsTest.run();
		bdsTest.checkStdout("Usage: Bds [options]");
	}

}
