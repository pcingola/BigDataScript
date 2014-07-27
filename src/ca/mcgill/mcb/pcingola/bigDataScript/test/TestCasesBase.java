package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;
import ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * All methods shared amongst test cases
 *
 * @author pcingola
 */
public class TestCasesBase extends TestCase {

	public static boolean debug = true;

	/**
	 * Check that a file compiles with expected errors
	 */
	void compileErrors(String fileName, String expectedErrors) {
		BigDataScript bigDataScript = compileTest(fileName);
		if (bigDataScript.getCompilerMessages().isEmpty()) fail("Expecting compilation errors in file '" + fileName + "', but none found!\n");
		Assert.assertEquals(expectedErrors.trim(), bigDataScript.getCompilerMessages().toString().trim());
	}

	/**
	 * Check that a file compiles without any errors
	 */
	void compileOk(String fileName) {
		BigDataScript bigDataScript = compileTest(fileName);
		if (!bigDataScript.getCompilerMessages().isEmpty()) fail("Compile errors in file '" + fileName + "':\n" + bigDataScript.getCompilerMessages());
	}

	/**
	 *
	 * Compile a file
	 */
	BigDataScript compileTest(String fileName) {
		String args[] = { fileName };
		BigDataScript bigDataScript = new BigDataScript(args);
		bigDataScript.compile();
		return bigDataScript;
	}

	/**
	 * Check that a file compiles without any errors, runs and a variable have its expected value
	 */
	void runAndCheck(String fileName, String varname, Object expectedValue) {
		String args[] = { fileName };
		runAndCheck(fileName, args, varname, expectedValue);
	}

	/**
	 * Check that a file compiles without any errors, runs and a variable have its expected value
	 */
	void runAndCheck(String fileName, String[] args, String varname, Object expectedValue) {
		// Compile and check for errors
		BigDataScript bigDataScript = new BigDataScript(args);
		bigDataScript.compile();
		if (!bigDataScript.getCompilerMessages().isEmpty()) fail("Compile errors in file '" + fileName + "':\n" + bigDataScript.getCompilerMessages());

		// Run
		bigDataScript = new BigDataScript(args);
		bigDataScript.run();
		ScopeSymbol ssym = bigDataScript.getProgramUnit().getRunScope().getSymbol(varname);

		if (debug) Gpr.debug("Program: " + fileName + "\t" + ssym);
		Assert.assertEquals(expectedValue.toString(), ssym == null ? "" : ssym.getValue().toString());
	}

	void runAndCheckExit(String fileName, int expectedExitValue) {
		// Compile
		String args[] = { fileName };
		BigDataScript bigDataScript = new BigDataScript(args);
		bigDataScript.compile();
		if (!bigDataScript.getCompilerMessages().isEmpty()) fail("Compile errors in file '" + fileName + "':\n" + bigDataScript.getCompilerMessages());

		// Run
		bigDataScript = new BigDataScript(args);
		int exitValue = bigDataScript.run();
		Assert.assertEquals(expectedExitValue, exitValue);
	}

	void runAndCheckMultiple(String fileName, HashMap<String, Object> expectedValues) {
		runAndCheckMultiple(fileName, expectedValues, new ArrayList<String>());
	}

	/**
	 * Check that a file compiles without any errors, runs and all variables have their expected values
	 * @param fileName
	 */
	void runAndCheckMultiple(String fileName, HashMap<String, Object> expectedValues, ArrayList<String> args) {
		// Prepare command line arguments
		args.add(0, fileName);
		String argsArray[] = args.toArray(new String[0]);

		// Compile
		BigDataScript bigDataScript = new BigDataScript(argsArray);
		bigDataScript.compile();
		if (!bigDataScript.getCompilerMessages().isEmpty()) fail("Compile errors in file '" + fileName + "':\n" + bigDataScript.getCompilerMessages());

		// Run
		bigDataScript = new BigDataScript(argsArray);
		bigDataScript.run();

		// Check all values
		for (String varName : expectedValues.keySet()) {
			Object expectedValue = expectedValues.get(varName);

			ScopeSymbol ssym = bigDataScript.getProgramUnit().getRunScope().getSymbol(varName);

			if (debug) Gpr.debug("Program: " + fileName + "\t" + ssym);
			if (!expectedValue.toString().equals(ssym.getValue().toString())) throw new RuntimeException("Variable '" + varName + "' does not match:\n"//
					+ "\tExpected : '" + expectedValue.toString() + "'" //
					+ "\tActual   : '" + ssym.getValue().toString() + "'" //
			);
		}
	}

	/**
	 * Check that a file recovers from a checkpoint and runs without errors
	 */
	void runAndCheckpoint(String fileName, String checkpointFileName, String varname, Object expectedValue) {
		runAndCheckpoint(fileName, checkpointFileName, varname, expectedValue, null);
	}

	/**
	 * Check that a file recovers from a checkpoint and runs without errors
	 */
	void runAndCheckpoint(String fileName, String checkpointFileName, String varname, Object expectedValue, Runnable runBeforeRecover) {
		// Compile
		String args[] = { fileName };
		BigDataScript bigDataScript = new BigDataScript(args);
		bigDataScript.compile();
		if (!bigDataScript.getCompilerMessages().isEmpty()) fail("Compile errors in file '" + fileName + "':\n" + bigDataScript.getCompilerMessages());

		// Run
		bigDataScript = new BigDataScript(args);
		bigDataScript.run();

		// Run something before checkpoint recovery?
		if (runBeforeRecover != null) runBeforeRecover.run();
		else {
			// Check that values match
			ScopeSymbol ssym = bigDataScript.getProgramUnit().getRunScope().getSymbol(varname);
			if (debug) Gpr.debug("Program: " + fileName + "\t" + ssym);
			Assert.assertEquals(expectedValue, ssym.getValue().toString());
		}

		//---
		// Recover from checkpoint
		//---
		String chpFileName = checkpointFileName;
		if (checkpointFileName == null) chpFileName = fileName + ".chp";
		if (debug) Gpr.debug("CheckPoint file name : " + chpFileName);
		String args2[] = { "-r", chpFileName };
		BigDataScript bigDataScript2 = new BigDataScript(args2);
		bigDataScript2.run();

		// Check that values match
		ScopeSymbol ssym = bigDataScript2.getProgramUnit().getRunScope().getSymbol(varname);
		if (debug) Gpr.debug("Program: " + fileName + "\t" + ssym);
		Assert.assertEquals(expectedValue, ssym.getValue().toString());
	}

	/**
	 * Check that StdErr has a string
	 */
	void runAndCheckStderr(String fileName, String expectedStderr) {
		String args[] = { fileName };

		// Compile
		BigDataScript bigDataScript = new BigDataScript(args);
		bigDataScript.compile();
		if (!bigDataScript.getCompilerMessages().isEmpty()) fail("Compile errors in file '" + fileName + "':\n" + bigDataScript.getCompilerMessages());

		PrintStream stderr = System.err;
		ByteArrayOutputStream captureStderr = new ByteArrayOutputStream();
		try {
			// Capture STDERR
			System.setErr(new PrintStream(captureStderr));

			// Run
			bigDataScript = new BigDataScript(args);
			bigDataScript.run();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			// Restore STDERR
			System.setErr(stderr);
		}

		// Check that the expected string is in STDERR
		if (debug) Gpr.debug("Program's stderr: '" + captureStderr + "'");
		int index = captureStderr.toString().indexOf(expectedStderr);
		if (index < 0) throw new RuntimeException("Error: Expeted string '" + expectedStderr + "' in STDERR not found.\nSTDERR:\n" + captureStderr + "\n");
	}

	/**
	 * Check that StdOut has a string
	 */
	String runAndCheckStdout(String fileName, String expectedStdout) {
		return runAndCheckStdout(fileName, expectedStdout, false);
	}

	/**
	 * Check that StdOut has a string (or that the string is not present if 'reverse')
	 */
	String runAndCheckStdout(String fileName, String expectedStdout, boolean reverse) {
		String args[] = { fileName };

		// Compile
		BigDataScript bigDataScript = new BigDataScript(args);
		bigDataScript.compile();
		if (!bigDataScript.getCompilerMessages().isEmpty()) fail("Compile errors in file '" + fileName + "':\n" + bigDataScript.getCompilerMessages());

		PrintStream stdout = System.out;
		ByteArrayOutputStream captureStdout = new ByteArrayOutputStream();
		try {
			// Capture STDOUT
			System.setOut(new PrintStream(captureStdout));

			// Run
			bigDataScript = new BigDataScript(args);
			bigDataScript.run();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			// Restore STDERR
			System.setOut(stdout);
		}

		// Check that the expected string is in STDERR
		if (debug) Gpr.debug("Program's stdout: '" + captureStdout + "'");
		int index = captureStdout.toString().indexOf(expectedStdout);
		if (!reverse && index < 0) throw new RuntimeException("Error: Expeted string '" + expectedStdout + "' in STDOUT not found.\nSTDOUT:\n" + captureStdout + "\n");
		if (reverse && index >= 0) throw new RuntimeException("Error: Expeted string '" + expectedStdout + "' in absent from STDOUT, but it was found.\nSTDOUT:\n" + captureStdout + "\n");
		return captureStdout.toString();
	}

}
