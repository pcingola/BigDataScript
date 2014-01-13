package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Quick test cases when creating a new feature...
 * 
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCase {

	public static boolean debug = false;

	/**
	 * Check that a file compiles with expected errors
	 * @param fileName
	 */
	void compileErrors(String fileName, String expectedErrors) {
		BigDataScript bigDataScript = compileTest(fileName);
		if (bigDataScript.getCompilerMessages().isEmpty()) fail("Expecting compilation errors in file '" + fileName + "', but none found!\n");
		Assert.assertEquals(expectedErrors, bigDataScript.getCompilerMessages().toString());
	}

	/**
	 * Check that a file compiles without any errors
	 * @param fileName
	 */
	void compileOk(String fileName) {
		BigDataScript bigDataScript = compileTest(fileName);
		if (!bigDataScript.getCompilerMessages().isEmpty()) fail("Compile errors in file '" + fileName + "':\n" + bigDataScript.getCompilerMessages());
	}

	/**
	 * 
	 * Compile a file
	 * @param fileName
	 * @return
	 */
	BigDataScript compileTest(String fileName) {
		String args[] = { fileName };
		BigDataScript bigDataScript = new BigDataScript(args);
		bigDataScript.compile();
		return bigDataScript;
	}

	/**
	 * Check that a file compiles without any errors, runs and a variable have its expected value
	 * @param fileName
	 */
	void runAndCheck(String fileName, String varname, Object expectedValue) {
		String args[] = { fileName };
		runAndCheck(fileName, args, varname, expectedValue);
	}

	/**
	 * Check that a file compiles without any errors, runs and a variable have its expected value
	 * @param fileName
	 */
	void runAndCheck(String fileName, String[] args, String varname, Object expectedValue) {
		// Compile
		BigDataScript bigDataScript = new BigDataScript(args);
		bigDataScript.compile();
		if (!bigDataScript.getCompilerMessages().isEmpty()) fail("Compile errors in file '" + fileName + "':\n" + bigDataScript.getCompilerMessages());

		// Run
		bigDataScript.run();
		ScopeSymbol ssym = bigDataScript.getProgramUnit().getScope().getSymbol(varname);

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
		bigDataScript.run();

		// Check all values
		for (String varName : expectedValues.keySet()) {
			Object expectedValue = expectedValues.get(varName);

			ScopeSymbol ssym = bigDataScript.getProgramUnit().getScope().getSymbol(varName);

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
	void runAndCheckpoint(String fileName, String varname, Object expectedValue) {
		runAndCheckpoint(fileName, varname, expectedValue, null);
	}

	/**
	 * Check that a file recovers from a checkpoint and runs without errors
	 */
	void runAndCheckpoint(String fileName, String varname, Object expectedValue, Runnable runBeforeRecover) {
		// Compile
		String args[] = { fileName };
		BigDataScript bigDataScript = new BigDataScript(args);
		bigDataScript.compile();
		if (!bigDataScript.getCompilerMessages().isEmpty()) fail("Compile errors in file '" + fileName + "':\n" + bigDataScript.getCompilerMessages());

		// Run
		bigDataScript.run();

		// Run something before checkpoint recovery?
		if (runBeforeRecover != null) runBeforeRecover.run();
		else {
			// Check that values match
			ScopeSymbol ssym = bigDataScript.getProgramUnit().getScope().getSymbol(varname);
			if (debug) Gpr.debug("Program: " + fileName + "\t" + ssym);
			Assert.assertEquals(expectedValue, ssym.getValue().toString());
		}

		//---
		// Recover from checkpoint
		//---
		String chpFileName = fileName + ".chp";
		if (debug) Gpr.debug("CheckPoint file name : " + chpFileName);
		String args2[] = { "-r", chpFileName };
		BigDataScript bigDataScript2 = new BigDataScript(args2);
		bigDataScript2.run();

		// Check that values match
		ScopeSymbol ssym = bigDataScript2.getProgramUnit().getScope().getSymbol(varname);
		if (debug) Gpr.debug("Program: " + fileName + "\t" + ssym);
		Assert.assertEquals(expectedValue, ssym.getValue().toString());
	}

	/**
	 * Check that StdErr has a string
	 * @param fileName
	 * @param expectedStderr
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

	@Test
	public void test87() {
		runAndCheck("test/run_87.bds", "cpus", "1");
	}

}
