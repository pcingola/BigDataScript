package org.bds.test;

import org.bds.Config;
import org.bds.util.Gpr;
import org.junit.Before;
import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Before
	public void beforeEachTest() {
		Config.reset();
		Config.get().load();
	}

	@Test
	public void testTestCasesCoverage06() {
		// Check that coverage is correctly computed
		Gpr.debug("Test");
		verbose = true;
		runTestCasesPass("test/test_case_run_06.bds");
		// TODO: Create '--coverage' command line option
		// TODO: Add bdsVm opcode
		// TODO: Add bdsVm opcode
	}

}
