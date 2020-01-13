package org.bds.test;

import org.bds.Bds;
import org.bds.Config;
import org.bds.run.Coverage;
import org.bds.util.Gpr;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

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

	void checkCoverageRatio(Bds bds, double coverageRatioExp) {
		Coverage coverage = bds.getBdsRun().getCoverageCounter();
		double coverageRatio = coverage.coverageRatio();
		double epsilon = 0.001;
		Assert.assertTrue("Coverage ration expected: " + coverageRatioExp + ", but got " + coverageRatio, Math.abs(coverageRatio - coverageRatioExp) < epsilon);
	}

	@Test
	public void testTestCasesCoverage06() {
		// Check that coverage is correctly computed: 100% coverage
		Gpr.debug("Test");
		Bds bds = runTestCasesPassCoverage("test/test_case_run_06.bds", 0.95);
		checkCoverageRatio(bds, 1.0);
	}

	@Test
	public void testTestCasesCoverage07() {
		// Check that coverage is correctly computed
		Gpr.debug("Test");
		Bds bds = runTestCasesPassCoverage("test/test_case_run_07.bds", 0.77);
		checkCoverageRatio(bds, 7.0 / 9.0);
	}

	@Test
	public void testTestCasesCoverage08() {
		// Check that coverage is correctly computed: Last line of the file not covered
		Gpr.debug("Test");
		verbose = true;
		Bds bds = runTestCasesPassCoverage("test/test_case_run_08.bds", 0.8);
		checkCoverageRatio(bds, 0.8);
	}

	@Test
	public void testTestCasesCoverage09() {
		// Check that coverage is correctly computed: 'if' statement in one line
		Gpr.debug("Test");
		Bds bds = runTestCasesPassCoverage("test/test_case_run_09.bds", 0.7);
		checkCoverageRatio(bds, 0.75);
	}

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
