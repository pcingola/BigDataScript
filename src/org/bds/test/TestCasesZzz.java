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
		runTestCasesPass("test/test_case_run_06.bds", true);
		// Create '--coverage' command line option
		// Add bdsVm opcode ('node_cov') that counts coverage
		// When invoked with '--coverage', bds should use 'node_cov' instead of 'node' to compile
		// TODO: Run test case/s
		// TODO: Coverage percent
		// TODO: Show results
	}

	//	public void testTestCasesCoverage07() {
	//		// Check that coverage is correctly computed: 'if' statement
	//		Gpr.debug("Test");
	//		runTestCasesPass("test/test_case_run_06.bds", true);
	//	}
	//
	//	public void testTestCasesCoverage08() {
	//		// Check that coverage is correctly computed: 'while' statements
	//		Gpr.debug("Test");
	//	}
	//
	//	public void testTestCasesCoverage09() {
	//		// Check that coverage is correctly computed: Class
	//		Gpr.debug("Test");
	//	}
	//
	//	public void testTestCasesCoverage10() {
	//		// Check that coverage is correctly computed: Private function
	//		Gpr.debug("Test");
	//	}
	//
	//	public void testTestCasesCoverage11() {
	//		// Check that coverage is correctly computed: Case
	//		Gpr.debug("Test");
	//	}
	//
	//	public void testTestCasesCoverage12() {
	//		// Check that coverage is correctly computed: Tri-operator
	//		Gpr.debug("Test");
	//	}

}
