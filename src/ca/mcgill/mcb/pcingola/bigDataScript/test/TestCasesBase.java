package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;

import ca.mcgill.mcb.pcingola.bigDataScript.Bds;
import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioners;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThreads;

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

	void runAndCheck(String fileName, HashMap<String, Object> expectedValues) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariables(expectedValues);
	}

	/**
	 * Check that a file compiles without any errors, runs and all variables have their expected values
	 */
	void runAndCheck(String fileName, HashMap<String, Object> expectedValues, ArrayList<String> argsAfterList) {
		String argsAfter[] = argsAfterList.toArray(new String[0]);
		BdsTest bdsTest = new BdsTest(fileName, null, argsAfter, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariables(expectedValues);
	}

	/**
	 * Check that a file compiles without any errors, runs and a variable have its expected value
	 */
	void runAndCheck(String fileName, String varname, Object expectedValue) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariable(varname, expectedValue);
	}

	/**
	 * Check that a file compiles without any errors, runs and a variable have its expected value
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
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
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
		return bdsTest.getSymbol(varname).getValue();
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

}
