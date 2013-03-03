package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.io.File;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class TestCasesCheckpoint extends TestCase {

	public static boolean debug = false;

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

	@Test
	public void test01() {
		runAndCheckpoint("test/checkpoint_01.bds", "i", "10");
	}

	@Test
	public void test02() {
		runAndCheckpoint("test/checkpoint_02.bds", "l", "15");
	}

	@Test
	public void test03() {
		runAndCheckpoint("test/checkpoint_03.bds", "s2", "After checkpoint 42");
	}

	@Test
	public void test04() {
		runAndCheckpoint("test/checkpoint_04.bds", "s", "one\teins");
	}

	@Test
	public void test05() {
		runAndCheckpoint("test/checkpoint_05.bds", "l0", "ONE");
	}

	/**
	 * How this test works:
	 * 		1) Try to delete a file that doesn't exits. Task fails, checkpoint is created and program finishes
	 * 		2) createFile is run: This creates the file to be deleted
	 * 		3) Checkpoint recovery, the task is re-executed. This time the file exists, so it runs OK. Variable 'b' is set to true
	 */
	@Test
	public void test06() {
		final String fileToDelete = "test/checkpoint_06.tmp";

		Runnable createFile = new Runnable() {

			@Override
			public void run() {
				// Create the file
				Gpr.debug("Creating file: '" + fileToDelete + "'");
				Gpr.toFile(fileToDelete, "Hello");
			}
		};

		// Make sure that the file doesn't exits
		(new File(fileToDelete)).delete();

		// Run test
		runAndCheckpoint("test/checkpoint_06.bds", "b", "true", createFile);
	}
}
