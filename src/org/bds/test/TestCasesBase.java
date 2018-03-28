package org.bds.test;

import java.util.List;
import java.util.Map;

import org.bds.Bds;
import org.bds.Config;
import org.bds.executioner.Executioners;
import org.bds.run.BdsThreads;
import org.bds.vm.BdsVm;
import org.bds.vm.VmAsm;
import org.junit.Before;

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

	void runAndCheck(int expectedExitCode, String fileName, String varname, Object expectedValue) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkExitCode(expectedExitCode);
		bdsTest.checkVariable(varname, expectedValue);
	}

	void runAndCheck(String fileName, Map<String, Object> expectedValues) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariables(expectedValues);
	}

	/**
	 * Check that a file compiles without any errors, runs and all variables have their expected values
	 */
	void runAndCheck(String fileName, Map<String, Object> expectedValues, List<String> argsAfterList) {
		String argsAfter[] = argsAfterList.toArray(new String[0]);
		BdsTest bdsTest = new BdsTest(fileName, null, argsAfter, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariables(expectedValues);
	}

	/**
	 * Check that a file compiles without any errors, runs and a variable have its expected map
	 */
	void runAndCheck(String fileName, String varname, Object expectedValue) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariable(varname, expectedValue);
	}

	/**
	 * Check that a file compiles without any errors, runs and a variable have its expected map
	 */
	void runAndCheck(String fileName, String[] args, String varname, Object expectedValue) {
		BdsTest bdsTest = new BdsTest(fileName, args, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariable(varname, expectedValue);
	}

	/**
	 * Check exit code
	 */
	void runAndCheckExit(String fileName, int expectedExitValue) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkExitCode(expectedExitValue);
	}

	/**
	 * Check that StdOut has a string
	 */
	void runAndCheckHelp(String fileName, String expectedStdout) {
		String argsAfter[] = { "-h" };
		BdsTest bdsTest = new BdsTest(fileName, null, argsAfter, verbose, debug);
		bdsTest.run();
		bdsTest.checkStdout(expectedStdout);
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
	void runAndCheckStderr(String fileName, String expectedStderr) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkStderr(expectedStderr);
	}

	/**
	 * Check that StdOut has a string
	 */
	String runAndCheckStdout(String fileName, String expectedStdout) {
		return runAndCheckStdout(fileName, expectedStdout, false);
	}

	/**
	 * Check that StdOut has a string (or that the string is NOT present if 'negate' is true)
	 */
	String runAndCheckStdout(String fileName, String expectedStdout, boolean negate) {
		BdsTest bdsTest = new BdsTest(fileName, true, debug);
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
	 * Run a bds program and capture stdout (while still showing it)
	 */
	String runAndReturnStdout(String fileName) {
		return runAndReturnStdout(fileName, null);
	}

	/**
	 * Run a bds program and capture stdout (while still showing it)
	 */
	String runAndReturnStdout(String fileName, String args[]) {
		BdsTest bdsTest = new BdsTest(fileName, args, true, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		return bdsTest.captureStdout.toString();
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

	/**
	 * Run test cases: Check that all test cases PASS
	 */
	void runTestCasesPass(String fileName) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.setTestCases(true);
		bdsTest.run();
		bdsTest.checkRunOk();
	}

	/**
	 * Check that a file compiles without any errors, runs and a variable have its expected map
	 */
	void runVmAndCheck(String fileName, String varname, Object expectedValue) {
		VmAsm vmasm = new VmAsm(fileName);
		vmasm.setDebug(debug);
		vmasm.setVerbose(verbose);
		BdsVm vm = vmasm.compile();
		vm.run();
	}

}
