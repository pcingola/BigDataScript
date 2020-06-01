package org.bds.test;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.bds.Bds;
import org.bds.Config;
import org.bds.data.Data;
import org.bds.data.DataRemote;
import org.bds.data.DataS3;
import org.bds.executioner.Executioners;
import org.bds.lang.type.Type;
import org.bds.lang.value.InterpolateVars;
import org.bds.lang.value.Value;
import org.bds.run.BdsThreads;
import org.bds.util.Gpr;
import org.bds.vm.BdsVm;
import org.bds.vm.BdsVmAsm;
import org.junit.Before;

import junit.framework.Assert;

/**
 * All methods shared amongst test cases
 *
 * @author pcingola
 */
public class TestCasesBase {

	public boolean debug = false;
	public boolean verbose = false;

	@Before
	public void before() {
		// Reset singletons
		Config.reset();
		Executioners.reset();
		BdsThreads.reset();
	}

	void checkInterpolate(String str, String strings[], String vars[]) {
		InterpolateVars iv = new InterpolateVars(null, null);
		iv.parse(str);
		if (verbose) {
			System.out.println("String: " + str);
			System.out.println("\tInterpolation result: |" + iv + "|");
		}

		// Special case: No variables to interpolate
		if (strings.length == 1 && vars[0].isEmpty()) {
			Assert.assertTrue(iv.isEmpty());
			return;
		}

		// Check strings
		for (int i = 0; i < strings.length; i++) {
			if (verbose) {
				System.out.print("\tIndex: " + i);
				System.out.print("\tstring.expected: " + strings[i] + "\tstring.actual: " + iv.getLiterals()[i]);
				System.out.println("\tvar.expected: " + vars[i] + "\tvar.actual: " + iv.getExpressions()[i]);
			}

			Assert.assertEquals(strings[i], iv.getLiterals()[i]);
			if (vars[i] != null && !vars[i].isEmpty()) Assert.assertEquals(vars[i], iv.getExpressions()[i].toString());
		}
	}

	/**
	 * Check a 'hello.txt' file in an S3 bucket
	 */
	void checkS3HelloTxt(String url, String path, String paren) {
		int objectSize = 12;
		long lastModified = 1437862027000L;

		Data d = Data.factory(url);
		d.setVerbose(verbose);
		d.setDebug(debug);
		long lastMod = d.getLastModified().getTime();
		if (verbose) Gpr.debug("Path: " + d.getPath() + "\tlastModified: " + lastMod + "\tSize: " + d.size());

		// Check some features
		Assert.assertTrue("Is S3?", d instanceof DataS3);
		Assert.assertEquals(path, d.getAbsolutePath());
		Assert.assertEquals(objectSize, d.size());
		Assert.assertEquals(lastModified, d.getLastModified().getTime());
		Assert.assertTrue("Is file?", d.isFile());
		Assert.assertFalse("Is directory?", d.isDirectory());

		// Download file
		boolean ok = d.download();
		Assert.assertTrue("Download OK", ok);
		Assert.assertTrue("Is downloaded?", d.isDownloaded());

		// Is it at the correct local file?
		Assert.assertEquals("/tmp/bds/s3/pcingola.bds/hello.txt", d.getLocalPath());
		Assert.assertEquals(path, d.getAbsolutePath());
		Assert.assertEquals(paren, d.getParent().toString());
		Assert.assertEquals("hello.txt", d.getName());

		// Check last modified time
		File file = new File(d.getLocalPath());
		long lastModLoc = file.lastModified();
		Assert.assertTrue("Last modified check:" //
				+ "\n\tlastMod    : " + lastMod //
				+ "\n\tlastModLoc : " + lastModLoc //
				+ "\n\tDiff       : " + (lastMod - lastModLoc)//
				, Math.abs(lastMod - lastModLoc) < 2 * DataRemote.CACHE_TIMEOUT);

		Assert.assertEquals(objectSize, file.length());
	}

	/**
	 * Check that a file compiles with expected errors
	 */
	void compileErrors(String fileName, String expectedErrors) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.compile();
		bdsTest.checkCompileError(expectedErrors);
	}

	/**
	 * Check that a file compiles without any errors
	 */
	void compileOk(String fileName) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.compile();
		bdsTest.checkCompileOk();
	}

	BdsTest runAndCheck(int expectedExitCode, String fileName, Map<String, Object> expectedValues) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkExitCode(expectedExitCode);
		bdsTest.checkVariables(expectedValues);
		return bdsTest;
	}

	BdsTest runAndCheck(int expectedExitCode, String fileName, String varname, Object expectedValue) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkExitCode(expectedExitCode);
		bdsTest.checkVariable(varname, expectedValue);
		return bdsTest;
	}

	BdsTest runAndCheck(String fileName, Map<String, Object> expectedValues) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariables(expectedValues);
		return bdsTest;
	}

	/**
	 * Check that a file compiles without any errors, runs and all variables have their expected values
	 */
	BdsTest runAndCheck(String fileName, Map<String, Object> expectedValues, List<String> argsAfterList) {
		String argsAfter[] = argsAfterList.toArray(new String[0]);
		BdsTest bdsTest = new BdsTest(fileName, null, argsAfter, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariables(expectedValues);
		return bdsTest;
	}

	/**
	 * Check that a file compiles without any errors, runs and a variable have its expected map
	 */
	BdsTest runAndCheck(String fileName, String varname, Object expectedValue) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariable(varname, expectedValue);
		return bdsTest;
	}

	/**
	 * Check that a file compiles without any errors, runs and a variable have its expected map
	 */
	BdsTest runAndCheck(String fileName, String[] args, String varname, Object expectedValue) {
		BdsTest bdsTest = new BdsTest(fileName, args, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariable(varname, expectedValue);
		return bdsTest;
	}

	BdsTest runAndCheckException(String fileName, String exceptionType) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunExitCodeFail();
		bdsTest.checkException(exceptionType);
		return bdsTest;
	}

	/**
	 * Check exit code
	 */
	BdsTest runAndCheckExit(String fileName, int expectedExitValue) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkExitCode(expectedExitValue);
		return bdsTest;
	}

	/**
	 * Check that StdOut has a string
	 */
	BdsTest runAndCheckHelp(String fileName, String expectedStdout) {
		String argsAfter[] = { "-h" };
		BdsTest bdsTest = new BdsTest(fileName, null, argsAfter, verbose, debug);
		bdsTest.run();
		bdsTest.checkStdout(expectedStdout);
		return bdsTest;
	}

	/**
	 * Check that a file recovers from a checkpoint and runs without errors
	 */
	Bds runAndCheckpoint(String fileName, String checkpointFileName, String varname, Object expectedValue) {
		return runAndCheckpoint(fileName, checkpointFileName, varname, expectedValue, null);
	}

	/**
	 * Check that a file recovers from a checkpoint and runs without errors
	 */
	Bds runAndCheckpoint(String fileName, String checkpointFileName, String varName, Object expectedValue, Runnable runBeforeRecover) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		return bdsTest.runAndCheckpoint(checkpointFileName, varName, expectedValue, runBeforeRecover);
	}

	/**
	 * Check that StdErr has a string
	 */
	BdsTest runAndCheckStderr(String fileName, String expectedStderr) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkStderr(expectedStderr);
		return bdsTest;
	}

	/**
	 * Check that StdOut has a string (or that the string is NOT present if 'negate' is true)
	 */
	String runAndCheckStdout(String fileName, List<String> expectedStdout, String args[], boolean negate) {
		BdsTest bdsTest = new BdsTest(fileName, args, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		for (String exp : expectedStdout)
			bdsTest.checkStdout(exp, negate);
		return bdsTest.captureStdout.toString();
	}

	/**
	 * Check that StdOut has a string
	 */
	String runAndCheckStdout(String fileName, String expectedStdout) {
		return runAndCheckStdout(fileName, expectedStdout, null, false);
	}

	String runAndCheckStdout(String fileName, String expectedStdout, boolean negate) {
		return runAndCheckStdout(fileName, expectedStdout, null, negate);
	}

	/**
	 * Check that StdOut has a string (or that the string is NOT present if 'negate' is true)
	 */
	String runAndCheckStdout(String fileName, String expectedStdout, String args[], boolean negate) {
		BdsTest bdsTest = new BdsTest(fileName, args, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkStdout(expectedStdout, negate);
		return bdsTest.captureStdout.toString();
	}

	/**
	 * Check that a file compiles without any errors, runs and obtains a variable
	 */
	Object runAndGet(String fileName, String varname) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		return bdsTest.getValue(varname);
	}

	/**
	 * Run and get latest report file
	 */
	String runAndGetReport(String fileName, boolean yaml) {
		String[] argsHtml = { "-reportHtml" };
		String[] argsYaml = { "-reportYaml" };
		String[] args = yaml ? argsYaml : argsHtml;

		BdsTest bdsTest = new BdsTest(fileName, args, verbose, debug);

		bdsTest.run();
		bdsTest.checkRunOk();
		String reportFileName = bdsTest.bds.getBdsRun().getBdsThread().getReportFile();

		// Sanity check
		Assert.assertNotNull("No report generated", reportFileName);
		Assert.assertTrue("Report file not found", Gpr.exists(reportFileName));

		String report = Gpr.readFile(reportFileName);
		if (debug) System.err.println("Report:\n" + report);

		return report;
	}

	/**
	 * Run a bds program and capture stdout (while still showing it)
	 */
	String runAndReturnStdout(String fileName) {
		return runAndReturnStdout(fileName, null);
	}

	/**
	 * Run a bds program and capture stdout (while still showing it)
	 */
	String runAndReturnStdout(String fileName, String args[]) {
		BdsTest bdsTest = new BdsTest(fileName, args, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		return bdsTest.captureStdout.toString();
	}

	/**
	 * Make sure the program runs OK
	 */
	void runOk(String fileName) {
		runAndCheckExit(fileName, 0);
	}

	/**
	 * Run test cases: Check that at least one test case FAILS
	 */
	void runTestCasesFail(String fileName) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.setTestCases(true);
		bdsTest.run();
		bdsTest.checkRunExitCodeFail();
	}

	Bds runTestCasesFailCoverage(String fileName, double coverageMin) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.setCoverage(true);
		bdsTest.setCoverageMin(coverageMin);
		bdsTest.setTestCases(true);
		bdsTest.run();
		bdsTest.checkRunExitCodeFail();
		return bdsTest.bds;
	}

	/**
	 * Run test cases: Check that all test cases PASS
	 */
	void runTestCasesPass(String fileName) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.setTestCases(true);
		bdsTest.run();
		bdsTest.checkRunOk();
	}

	Bds runTestCasesPassCoverage(String fileName, double coverageMin) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.setCoverage(true);
		bdsTest.setCoverageMin(coverageMin);
		bdsTest.setTestCases(true);
		bdsTest.run();
		bdsTest.checkRunOk();
		return bdsTest.bds;
	}

	/**
	 * Check that a file compiles without any errors, runs the VM
	 * and checks that a variable has the expected value
	 */
	void runVmAndCheck(String fileName, String varname, Object expectedValue) {
		runVmAndCheck(fileName, varname, expectedValue, null);
	}

	/**
	 * Check that a file compiles without any errors, runs the VM
	 * and checks that a variable has the expected value.
	 * Add all 'types' during compilation
	 */
	void runVmAndCheck(String fileName, String varname, Object expectedValue, List<Type> types) {
		BdsVmAsm vmasm = new BdsVmAsm(fileName);
		vmasm.setDebug(debug);
		vmasm.setVerbose(verbose);
		if (types != null) types.forEach(t -> vmasm.addType(t));
		BdsVm vm = vmasm.compile();
		vm.run();

		// Check value
		Value val = vm.getValue(varname);
		Assert.assertTrue("Variable '" + varname + "' not found ", val != null);
		Assert.assertEquals( //
				"Variable '" + varname + "' has different value than expeced:\n" //
						+ "\tExpected value : " + expectedValue //
						+ "\tReal value     : " + val //
				, expectedValue.toString() //
				, val.toString() //
		);
	}

}
