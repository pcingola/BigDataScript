package org.bds.test;

import java.util.ArrayList;
import java.util.HashMap;

import org.bds.util.Gpr;
import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test40() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("file", "zzz.txt");
		expectedValues.put("opt", "true");
		expectedValues.put("num", "42");
		expectedValues.put("rnum", "3.1415");
		expectedValues.put("args", "[-file, zzz.txt, -num, 42, -rnum, 3.1415, -opt, -notProcessed, more, arguments]");

		// Arguments to add after program name
		ArrayList<String> argsAfter = new ArrayList<>();

		argsAfter.add("-file");
		argsAfter.add("zzz.txt");

		argsAfter.add("-num");
		argsAfter.add("42");

		argsAfter.add("-rnum");
		argsAfter.add("3.1415");

		argsAfter.add("-opt");

		argsAfter.add("-notProcessed");
		argsAfter.add("more");
		argsAfter.add("arguments");

		runAndCheck("test/run_40.bds", expectedValues, argsAfter);
	}

	/**
	 * Show help when there are no arguments
	 */
	@Test
	public void test125c_automatic_help() {
		Gpr.debug("Test");
		String output = "Command line options 'run_125c.bds' :\n" //
				+ "\t-mean <int>                                  : Help for argument 'mean' should be printed here\n" //
				+ "\t-min <int>                                   : Help for argument 'min' should be printed here\n" //
				+ "\t-num <int>                                   : Number of times 'hi' should be printed\n" //
				+ "\t-salutation <string>                         : Salutation to use\n" //
				+ "\t-someVeryLongCommandLineArgumentName <bool>  : This command line argument has a really long name\n" //
				+ "\t-useTab <bool>                               : Use tab before printing line\n" //
				+ "\n" //
		;

		runAndCheckHelp("test/run_125c.bds", output);
	}

	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// FUTURE TEST CASES
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	//	@Test
	//	public void test200() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_200.bds", "", "");
	//	}
	//
	//	@Test
	//	public void test201() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_201.bds", "", "");
	//	}
	//
	//	@Test
	//	public void test202() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_202.bds", "", "");
	//	}
	//
	//	@Test
	//	public void test203() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_203.bds", "", "");
	//	}
	//
	//	@Test
	//	public void test204() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_204.bds", "", "");
	//	}

}
