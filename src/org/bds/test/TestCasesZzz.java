package org.bds.test;

import org.bds.Config;
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

	//	void checkCoverageRatio(Bds bds, double coverageRatioExp) {
	//		Coverage coverage = bds.getBdsRun().getCoverageCounter();
	//		double coverageRatio = coverage.coverageRatio();
	//		double epsilon = 0.001;
	//		Assert.assertTrue("Coverage ration expected: " + coverageRatioExp + ", but got " + coverageRatio, Math.abs(coverageRatio - coverageRatioExp) < epsilon);
	//	}
	//
	//	@Test
	//	public void testTestCasesCoverage06() {
	//		// Check that coverage is correctly computed: 100% coverage
	//		Gpr.debug("Test");
	//		Bds bds = runTestCasesPassCoverage("test/test_case_run_06.bds", 0.95);
	//		checkCoverageRatio(bds, 1.0);
	//	}
	//
	//	@Test
	//	public void testTestCasesCoverage07() {
	//		// Check that coverage is correctly computed
	//		Gpr.debug("Test");
	//		Bds bds = runTestCasesPassCoverage("test/test_case_run_07.bds", 0.77);
	//		checkCoverageRatio(bds, 7.0 / 9.0);
	//	}
	//
	//	@Test
	//	public void testTestCasesCoverage07_fail() {
	//		// Check that coverage is correctly computed
	//		Gpr.debug("Test");
	//		runTestCasesFailCoverage("test/test_case_run_07.bds", 0.8);
	//	}
	//
	//	@Test
	//	public void testTestCasesCoverage08() {
	//		// Check that coverage is correctly computed: Last line of the file not covered
	//		Gpr.debug("Test");
	//		Bds bds = runTestCasesPassCoverage("test/test_case_run_08.bds", 0.8);
	//		checkCoverageRatio(bds, 0.8);
	//	}
	//
	//	@Test
	//	public void testTestCasesCoverage09() {
	//		// Check that coverage is correctly computed: 'if' statement in one line
	//		Gpr.debug("Test");
	//		Bds bds = runTestCasesPassCoverage("test/test_case_run_09.bds", 0.7);
	//		checkCoverageRatio(bds, 0.75);
	//	}
	//
	//	@Test
	//	public void testTestCasesCoverage10() {
	//		// Check that coverage is correctly computed: 'while' statements
	//		Gpr.debug("Test");
	//		Bds bds = runTestCasesPassCoverage("test/test_case_run_10.bds", 0.8);
	//		checkCoverageRatio(bds, 9.0 / 11.0);
	//	}
	//
	//	@Test
	//	public void testTestCasesCoverage11() {
	//		// Check that coverage is correctly computed: for loop
	//		Gpr.debug("Test");
	//		Bds bds = runTestCasesPassCoverage("test/test_case_run_11.bds", 0.8);
	//		checkCoverageRatio(bds, 8.0 / 9.0);
	//	}
	//
	//	@Test
	//	public void testTestCasesCoverage12() {
	//		// Check that coverage is correctly computed: Private function
	//		Gpr.debug("Test");
	//		Bds bds = runTestCasesPassCoverage("test/test_case_run_12.bds", 0.99);
	//		checkCoverageRatio(bds, 1.0);
	//	}
	//
	//	@Test
	//	public void testTestCasesCoverage13() {
	//		// Check that coverage is correctly computed: Private function
	//		Gpr.debug("Test");
	//		Bds bds = runTestCasesPassCoverage("test/test_case_run_13.bds", 0.88);
	//		checkCoverageRatio(bds, 8.0 / 9.0);
	//	}
	//
	//	@Test
	//	public void testTestCasesCoverage14() {
	//		// Check that coverage is correctly computed: Switch / Case
	//		Gpr.debug("Test");
	//		Bds bds = runTestCasesPassCoverage("test/test_case_run_14.bds", 0.64);
	//		checkCoverageRatio(bds, 9.0 / 14.0);
	//	}
	//
	//	@Test
	//	public void testTestCasesCoverage15() {
	//		// Check that coverage is correctly computed: Ternary operator
	//		Gpr.debug("Test");
	//		Bds bds = runTestCasesPassCoverage("test/test_case_run_15.bds", 0.75);
	//		checkCoverageRatio(bds, 0.75);
	//	}
	//
	//	@Test
	//	public void testTestCasesCoverage16() {
	//		// Check that coverage is correctly computed: Ternary operator
	//		Gpr.debug("Test");
	//		Bds bds = runTestCasesPassCoverage("test/test_case_run_16.bds", 0.99);
	//		checkCoverageRatio(bds, 1.0);
	//	}
	//
	//	@Test
	//	public void testTestCasesCoverage17() {
	//		// Check that coverage is correctly computed: include
	//		Gpr.debug("Test");
	//		Bds bds = runTestCasesPassCoverage("test/test_case_run_17.bds", 0.8);
	//		checkCoverageRatio(bds, 70.0 / 84.0);
	//	}

	@Test
	public void test131_chdir_fileMethods() {
		Gpr.debug("Test");
		String out = ""//
				+ "chdir_test_file_01.txt\tread:FILE_01\n" //
				+ "chdir_test_file_01.txt\treadLines:[FILE_01]\n" //
				+ "chdir_test_file_01.txt\texists:true\n" //
				+ "chdir_test_file_01.txt\tisDir:false\n" //
				+ "chdir_test_file_01.txt\tisEmpty:false\n" //
				+ "chdir_test_file_01.txt\tisFile:true\n" //
				+ "chdir_test_file_01.txt\tcanRead:true\n" //
				+ "chdir_test_file_01.txt\tcanWrite:true\n" //
				+ "\n" //
				+ "----------\n" //
				+ "chdir_test_file_02.txt\tread:FILE_02\n" //
				+ "chdir_test_file_02.txt\treadLines:[FILE_02]\n" //
				+ "chdir_test_file_02.txt\texists:true\n" //
				+ "chdir_test_file_02.txt\tisDir:false\n" //
				+ "chdir_test_file_02.txt\tisEmpty:false\n" //
				+ "chdir_test_file_02.txt\tisFile:true\n" //
				+ "chdir_test_file_02.txt\tcanRead:true\n" //
				+ "chdir_test_file_02.txt\tcanWrite:true\n" //
		;

		String outreal = runAndReturnStdout("test/run_131.bds");
		Assert.assertEquals(out, outreal);
	}

}
