package org.bds.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;

import org.bds.Bds;
import org.bds.compile.CompilerMessages;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueClass;
import org.bds.osCmd.TeeOutputStream;
import org.bds.run.RunState;
import org.bds.util.Gpr;
import org.bds.vm.BdsVm;

import junit.framework.Assert;

/**
 * BDS test cases: BdsCompiler or run a bds program and store exitCode, STDOUT, STDERR, etc.
 *
 * @author pcingola
 */
public class BdsTest {

	public boolean coverage;
	public double coverageMin;
	public boolean debug;
	public boolean log;
	public boolean verbose;
	public boolean testCases; // Is this a bds-test? i.e. should it run as 'bds --test'?
	public Boolean compileOk;
	public String args[]; // Command line arguments (for 'bds', not for the script)
	public String scriptArgs[]; // Command line arguments for the bds script
	public String fileName;
	public CompilerMessages compilerMessages;
	public Bds bds;
	public Integer exitCode;
	public ByteArrayOutputStream captureStdout, captureStderr; // Capture STDOUT & STDERR
	public TeeOutputStream teeStdout, teeStderr;
	PrintStream stdout, stderr; // Store original STDOUT & STDERR
	public RunState runState;

	public BdsTest(String fileName, boolean verbose, boolean debug) {
		this(fileName, null, null, verbose, debug);
	}

	public BdsTest(String fileName, String args[], boolean verbose, boolean debug) {
		this(fileName, args, null, verbose, debug);
	}

	public BdsTest(String fileName, String args[], String scriptArgs[], boolean verbose, boolean debug) {
		this.fileName = fileName;
		this.args = args;
		this.scriptArgs = scriptArgs;
		this.verbose = verbose;
		this.debug = debug;
	}

	/**
	 * Create 'command'
	 */
	public void bds(boolean compileOnly) {
		ArrayList<String> l = new ArrayList<>();

		// Add command line options
		if (verbose) l.add("-v");
		if (debug) l.add("-d");
		if (log) l.add("-log");

		if (compileOnly) l.add("-compile");

		// Is this a 'test case' run?
		if (testCases) l.add("-t");

		// Set coverage
		if (coverage) l.add("-coverage");

		// Coverage ratio
		if (coverageMin > 0) {
			l.add("-coverageMin");
			l.add(coverageMin + "");
		}

		if (args != null) {
			for (String arg : args)
				l.add(arg);
		}

		l.add(fileName); // Add file

		// Script arguments
		if (scriptArgs != null) {
			for (String arg : scriptArgs)
				l.add(arg);
		}

		args = l.toArray(new String[0]);

		// Create command
		bds = new Bds(args);
		bds.getBdsRun().setStackCheck(true);
	}

	/**
	 * Show captured STDOUT & STDERR
	 */
	protected void captureShow() {
		if (!(verbose || debug)) {
			stdout.print("STDOUT ('" + fileName + "'):\n" + Gpr.prependEachLine("\t", captureStdout.toString()));
			stderr.print("STDERR ('" + fileName + "'):\n" + Gpr.prependEachLine("\t", captureStderr.toString()));
		}
	}

	protected void captureStart() {
		// Capture STDOUT
		stdout = System.out; // Store original stdout
		captureStdout = new ByteArrayOutputStream();
		teeStdout = new TeeOutputStream(stdout, captureStdout);

		// Capture STDERR
		stderr = System.err;
		captureStderr = new ByteArrayOutputStream();
		teeStderr = new TeeOutputStream(stderr, captureStderr);

		boolean show = verbose || debug;

		// Set STDOUT & STDERR
		System.setOut(new PrintStream(show ? teeStdout : captureStdout));
		System.setErr(new PrintStream(show ? teeStderr : captureStderr));
	}

	/**
	 * Stop capturing STDOUT & STDERR (restore original)
	 */
	protected void captureStop() {
		// Restore STDOUT & STDERR
		System.setOut(stdout);
		System.setErr(stderr);

		try {
			if (teeStdout != null) teeStdout.close();
			if (teeStderr != null) teeStderr.close();
			teeStdout = teeStderr = null;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Check that compiler errors are found during compiling process
	 */
	public void checkCompileError(String expectedErrors) {
		Assert.assertFalse(errMsg("Expecting compilation errors, none found (program compiled OK)"), compileOk);
		if (compilerMessages.toString().indexOf(expectedErrors) >= 0) return;
		Assert.assertEquals(errMsg("Expecting compilation errors not found"), expectedErrors.trim(), compilerMessages.toString().trim());
	}

	/**
	 * Check that the file was compiled OK
	 */
	public void checkCompileOk() {
		if (!compilerMessages.isEmpty()) Assert.fail("BdsCompiler errors in file '" + fileName + "':\n" + compilerMessages);
		if (compileOk != null) Assert.assertTrue(errMsg("There was an error while compiling"), compileOk);
	}

	public void checkException(String exceptionType) {
		BdsVm vm = bds.getBdsRun().getVm();
		ValueClass evc = vm.getExceptionValue();
		Assert.assertTrue(errMsg("No exception found"), evc != null);

		String exType = evc.getType().getCanonicalName();
		Assert.assertEquals(errMsg("Exception type does not match, expecting '" + exceptionType + "', got '" + exType + "'"), exType, exceptionType);
	}

	/**
	 * Check exit code
	 */
	public void checkExitCode(int expectedExitCode) {
		Assert.assertTrue(errMsg("No exit value (program was not run)"), exitCode != null);
		Assert.assertEquals(errMsg("Expecting exit code '" + expectedExitCode + "', but it was '" + exitCode + "'"), expectedExitCode, (int) exitCode);
	}

	/**
	 * Check that the program run and finished with a failed exit code
	 */
	public void checkRunExitCodeFail() {
		checkCompileOk();
		checkRunState(RunState.FINISHED);
		checkExitCode(1);
	}

	/**
	 * Check that the program run and finished OK
	 */
	public void checkRunOk() {
		checkCompileOk();
		checkRunState(RunState.FINISHED);
		checkExitCode(0);
	}

	/**
	 * Check that RunState matches ou expectations
	 */
	public void checkRunState(RunState expectedRunState) {
		Assert.assertEquals(errMsg("Expecting rRunState '" + expectedRunState + "', but it was '" + runState + "'") //
				, expectedRunState //
				, runState//
		);
	}

	public void checkStderr(String expectedStderr) {
		int index = captureStderr.toString().indexOf(expectedStderr);
		Assert.assertTrue(errMsg("Error: Expeted string '" + expectedStderr + "' in STDERR not found"), index >= 0);
	}

	public void checkStdout(String expectedStdout) {
		checkStdout(expectedStdout, false);
	}

	/**
	 * Check that the script's STDOUT includes 'expectedStdout' exactly one time
	 * (or that it does NOT include it, if 'negate' is set)
	 * @param expectedStdout
	 * @param negate
	 */
	public void checkStdout(String expectedStdout, boolean negate) {
		int count = countMatchesStdout(expectedStdout);

		if (negate) Assert.assertFalse(errMsg("Error: NOT expected string '" + expectedStdout + "' in STDOUT, but it was found"), count > 0);
		else {
			if (count <= 0) {
				// Not found? Print differences
				String out = captureStdout.toString();
				printDiffLines(expectedStdout, out);
			}
			Assert.assertTrue(errMsg("Error: Expected string '" + expectedStdout + "' in STDOUT not found"), count > 0);
			Assert.assertTrue(errMsg("Error: Expected string '" + expectedStdout + "' in STDOUT only one time, but was found " + count + " times."), count == 1);
		}
	}

	/**
	 * Check a variable's map
	 */
	public void checkVariable(String varname, Object expectedValue) {
		Value val = getValue(varname);
		if (expectedValue != null) Assert.assertTrue(errMsg("Variable '" + varname + "' not found "), val != null);

		// Convert values to string
		String evstr = expectedValue != null ? expectedValue.toString() : "null";
		String vstr = val != null ? val.toString() : "null";

		Assert.assertEquals( //
				errMsg("Variable '" + varname + "' has different value than expeced.\n" //
						+ "\tExpected value : '" + evstr + "'" //
						+ "\tReal value     : '" + vstr) + "'" //
				, evstr //
				, vstr //
		);
	}

	/**
	 * Check all variables in the hash
	 */
	public void checkVariables(Map<String, Object> expectedValues) {
		// Check all values
		for (String varName : expectedValues.keySet()) {
			Object expectedValue = expectedValues.get(varName);

			Value val = getValue(varName);
			Assert.assertTrue(errMsg("Missing variable '" + varName + "'"), val != null);

			if (!expectedValue.toString().equals(val.toString())) {
				Assert.assertEquals(errMsg("Variable '" + varName + "' does not match:\n"//
						+ "\tExpected : '" + expectedValue.toString() + "'" //
						+ "\tActual   : '" + val.toString() + "'" //
				) //
						, expectedValue.toString() //
						, val.toString() //
				);
			}
		}
	}

	/**
	 * Check a variable's map
	 */
	public void checkVariableVm(BdsVm vm, String varname, Object expectedValue) {
		Value val = vm.getValue(varname);
		Assert.assertTrue(errMsg("Variable '" + varname + "' not found "), val != null);
		Assert.assertEquals( //
				errMsg("Variable '" + varname + "' has different map than expeced:\n" //
						+ "\tExpected map : " + expectedValue //
						+ "\tReal map     : " + val) //
				, expectedValue.toString() //
				, val.toString() //
		);
	}

	/**
	 * BdsCompiler code
	 */
	public boolean compile() {
		if (bds == null) bds(true); // Create command

		compileOk = false;
		try {
			captureStart(); // Capture STDOUT & STDERR
			compileOk = bds.run() == 0; // Compiled OK?
			compilerMessages = CompilerMessages.get();
		} catch (Throwable t) {
			captureShow(); // Make sure STDOUT & STDERR have been shown
			captureStop();
			throw new RuntimeException(t);
		} finally {
			captureStop();
		}

		return compileOk;
	}

	/**
	 * Count how many time the 'match' is found in 'str'
	 */
	int countMatches(String str, String toMatch) {
		int count = -1, index = -1;
		do {
			count++;
			index = str.indexOf(toMatch, index + 1);
		} while (index >= 0);
		return count;
	}

	/**
	 * Count how many time the 'expectedStdout' is found in 'stdout'
	 */
	int countMatchesStdout(String expectedStdout) {
		return countMatches(captureStdout.toString(), expectedStdout);
	}

	/**
	 * Create an error message
	 */
	String errMsg(String msg) {
		StringBuilder sb = new StringBuilder();

		sb.append("ERROR: " + msg + "\n");
		sb.append("\t" + toString());

		return sb.toString();
	}

	/**
	 * Get a symbol
	 */
	public Value getValue(String name) {
		return bds.getBdsRun().getScope().getValue(name);
	}

	public boolean isLog() {
		return log;
	}

	/**
	 * Show differences
	 */
	void printDiffLines(String expout, String out) {
		String[] expoutLines = expout.split("\n");
		String[] outLines = out.split("\n");
		for (int i = 0; i < expoutLines.length; i++) {
			if (outLines.length <= i) {
				System.err.println("Line " + i + "\n\tWARNING: Output does not have line number " + i + "\n\t" + expoutLines[i]);
			} else if (!expoutLines[i].equals(outLines[i])) {
				System.err.println("Line " + i + "\n\t" + outLines[i] + "\n\t" + expoutLines[i]);
			}
		}
	}

	/**
	 * Run command
	 */
	public void run() {
		if (bds == null) bds(false); // Create command

		try {
			captureStart(); // Capture STDOUT & STDERR
			exitCode = bds.run(); // Run
			compilerMessages = CompilerMessages.get(); // Any compile errors?
			if (bds.getBdsRun().getBdsThread() != null) runState = bds.getBdsRun().getBdsThread().getRunState(); // Get final RunState
		} catch (Throwable t) {
			captureShow(); // Make sure STDOUT & STDERR have been shown
			captureStop();
			t.printStackTrace();
			throw new RuntimeException(t);
		} finally {
			captureStop();
		}
	}

	/**
	 * Run a file and check that the expected variable matches a result
	 */
	public void runAndCheck(String varname, Object expectedValue) {
		run();
		checkRunOk();
		checkVariable(varname, expectedValue);
	}

	/**
	 * Check that a file recovers from a checkpoint and runs without errors
	 */
	public Bds runAndCheckpoint(String checkpointFileName, String varName, Object expectedValue, Runnable runBeforeRecover) {
		// Run
		run();
		checkCompileOk();

		// Run something before checkpoint recovery?
		if (runBeforeRecover != null) runBeforeRecover.run();
		else if (varName != null) checkVariable(varName, expectedValue);

		//---
		// Recover from checkpoint
		//---
		String chpFileName = checkpointFileName;
		if (checkpointFileName == null) chpFileName = fileName + ".chp";
		if (verbose) System.err.println("\n\n\nRecovering from checkpoint file '" + chpFileName + "'\n\n\n");
		if (debug) Gpr.debug("CheckPoint file name : " + chpFileName);
		String args2[] = { "-r", chpFileName };
		String args2v[] = { "-v", "-r", chpFileName };
		Bds bigDataScript2 = new Bds(verbose ? args2v : args2);
		bigDataScript2.getBdsRun().setStackCheck(true);
		bigDataScript2.run();

		// Check variable's map on the recovered (checkpoint run) program
		if (varName != null) {
			Value val = bigDataScript2.getBdsRun().getScope().getValue(varName);
			Assert.assertTrue(errMsg("Variable '" + varName + "' not found "), val != null);
			Assert.assertEquals( //
					errMsg("Variable '" + varName + "' has different value than expeced:\n" //
							+ "\tExpected value : " + expectedValue //
							+ "\tReal value     : " + val) //
					, expectedValue.toString() //
					, val.toString() //
			);
		}

		return bigDataScript2;
	}

	public void setCoverage(boolean coverage) {
		this.coverage = coverage;
	}

	public void setCoverageMin(double coverageMin) {
		this.coverageMin = coverageMin;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	public void setTestCases(boolean testCases) {
		this.testCases = testCases;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Program: " + fileName + "\n");

		sb.append("\tArguments:\n");
		for (int i = 0; i < args.length; i++)
			sb.append("\t\targs[" + i + "]: " + args[i] + "\n");

		// Compilation errors / messages?
		if (compileOk != null) sb.append("\tCompiled OK: " + compileOk + "\n");
		if (!compilerMessages.isEmpty()) sb.append("\tCompile messages: " + compilerMessages.toString() + "\n");

		if (exitCode != null) sb.append("\tExit code: " + exitCode + "\n");

		// STDOUT and STDERR not already shown? Add them
		if (!(verbose || debug)) {
			sb.append("\tSTDOUT:\n" + Gpr.prependEachLine("\t\t|", captureStdout.toString()) + "\n");
			sb.append("\tSTDERR:\n" + Gpr.prependEachLine("\t\t|", captureStderr.toString()) + "\n");
		}

		return sb.toString();

	}

}
