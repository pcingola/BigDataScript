package ca.mcgill.mcb.pcingola.bigDataScript.test;

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
		// Compile
		String args[] = { fileName };
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

	@Test
	public void test42() {
		compileOk("test/test42.bds");
	}

}
