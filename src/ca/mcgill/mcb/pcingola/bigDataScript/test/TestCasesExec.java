package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Test cases that require BDS code execution and check results
 * 
 * Note: These test cases requires that the BDS code is correctly parsed, compiled and executes. 
 * 
 * @author pcingola
 *
 */
public class TestCasesExec extends TestCase {

	public static boolean debug = false;

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
			Assert.assertEquals(expectedValue.toString(), ssym.getValue().toString());
		}
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	@Test
	public void test01() {
		runAndCheck("test/run_01.bds", "i", 2L);
	}

	@Test
	public void test02() {
		runAndCheck("test/run_02.bds", "i", 9L);
	}

	@Test
	public void test03() {
		runAndCheck("test/run_03.bds", "i", 10L);
	}

	@Test
	public void test03_2() {
		runAndCheck("test/run_03.bds", "j", 9L);
	}

	@Test
	public void test04() {
		runAndCheck("test/run_04.bds", "i", 10L);
	}

	@Test
	public void test04_2() {
		runAndCheck("test/run_04.bds", "j", 11L);
	}

	@Test
	public void test05() {
		runAndCheck("test/run_05.bds", "i", 10L);
	}

	@Test
	public void test06() {
		runAndCheck("test/run_06.bds", "i", 1L);
	}

	@Test
	public void test06_2() {
		runAndCheck("test/run_06.bds", "r1", 1.0);
	}

	@Test
	public void test06_3() {
		runAndCheck("test/run_06.bds", "r2", 5.0);
	}

	@Test
	public void test07() {
		runAndCheck("test/run_07.bds", "j", 4L);
	}

	@Test
	public void test08() {
		runAndCheck("test/run_08.bds", "j", 5L);
	}

	@Test
	public void test09() {
		runAndCheck("test/run_09.bds", "i", 5L);
	}

	@Test
	public void test10() {
		runAndCheck("test/run_10.bds", "i", 4L);
	}

	@Test
	public void test11() {
		runAndCheck("test/run_11.bds", "i", 6L);
	}

	@Test
	public void test12() {
		runAndCheck("test/run_12.bds", "i", 1L);
	}

	@Test
	public void test13() {
		runAndCheck("test/run_13.bds", "h", 8L);
	}

	@Test
	public void test14() {
		runAndCheck("test/run_14.bds", "s", "this is string interpolation: int i = 42 and str = \"hi\" and both hi42");
	}

	@Test
	public void test15_2() {
		runAndCheck("test/run_15.bds", "li2", "[apple, orange, bannana]");
	}

	@Test
	public void test15_3() {
		runAndCheck("test/run_15.bds", "li3", "[apple, orange, 1]");
	}

	@Test
	public void test15_4() {
		runAndCheck("test/run_15.bds", "li4", "[apple, orange, 3.14]");
	}

	@Test
	public void test15_5() {
		runAndCheck("test/run_15.bds", "li5", "[apple, orange, false]");
	}

	@Test
	public void test15_6() {
		runAndCheck("test/run_15.bds", "li6", "[apple, orange, i=10hihihi]");
	}

	@Test
	public void test16() {
		runAndCheck("test/run_16.bds", "ss", "onetwothree");
	}

	@Test
	public void test17() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("s", " HEllo ");
		expectedValues.put("s1", "HEllo");
		expectedValues.put("s2", " hello ");
		expectedValues.put("s3", " HELLO ");
		expectedValues.put("s4", "Ello ");
		expectedValues.put("s5", "Ell");
		expectedValues.put("s6", " HEllo ");
		expectedValues.put("s7", "");
		expectedValues.put("s8", " HEzo ");
		expectedValues.put("i1", 7);
		expectedValues.put("i2", 3);
		expectedValues.put("i3", 4);
		expectedValues.put("i4", -1);
		expectedValues.put("i5", -1);
		expectedValues.put("ss", "hello");
		expectedValues.put("b1", true);
		expectedValues.put("b2", false);
		expectedValues.put("b3", true);
		expectedValues.put("b4", false);
		runAndCheckMultiple("test/run_17.bds", expectedValues);
	}

	@Test
	public void test18() {
		runAndCheck("test/run_18.bds", "s1", "three");
	}

	@Test
	public void test18_2() {
		runAndCheck("test/run_18.bds", "s3", "three");
	}

	@Test
	public void test19() {
		runAndCheck("test/run_19.bds", "h", "one");
	}

	@Test
	public void test20() {
		runAndCheck("test/run_20.bds", "h", "dos");
	}

	@Test
	public void test21() {
		runAndCheck("test/run_21.bds", "l1", "line 1\nline 2\nline 3\n");
	}

	@Test
	public void test21_2() {
		runAndCheck("test/run_21.bds", "l2", "line 2");
	}

	@Test
	public void test22() {
		runAndCheck("test/run_22.bds", "l2", "file_3.txt");
	}

	@Test
	public void test23() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("ltsize", 2);
		expectedValues.put("lh", "one");
		expectedValues.put("lt0", "two");
		expectedValues.put("lt1", "three");
		runAndCheckMultiple("test/run_23.bds", expectedValues);
	}

	@Test
	public void test24() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("p", 3);
		expectedValues.put("s", 3);
		expectedValues.put("l0", 1);
		expectedValues.put("l1", 2);
		expectedValues.put("l2", 4);
		runAndCheckMultiple("test/run_24.bds", expectedValues);
	}

	@Test
	public void test25() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("l0", 1);
		expectedValues.put("l1", 2);
		expectedValues.put("l2", 3);
		runAndCheckMultiple("test/run_25.bds", expectedValues);
	}

	@Test
	public void test26() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("l0", 1);
		expectedValues.put("l1", 2);
		expectedValues.put("l2", 3);
		expectedValues.put("l3", 4);
		expectedValues.put("l4", 5);
		expectedValues.put("s", 5);
		runAndCheckMultiple("test/run_26.bds", expectedValues);
	}

	@Test
	public void test27() {
		Timer timer = new Timer();
		timer.start();
		runAndCheck("test/run_27.bds", "timeout", "1"); // 2 seconds timeout
		Assert.assertTrue(timer.elapsed() < 3 * 1000); // We should finish in less than 3 secs (the program waits 60secs)
	}

	@Test
	public void test28() {
		runAndCheck("test/run_28.bds", "events", "[done]");
	}

	@Test
	public void test29() {
		runAndCheck("test/run_29.bds", "events", "[runnning, wait, done]");
	}

	@Test
	public void test30() {
		runAndCheck("test/run_30.bds", "events", "[runnning, wait, done]");
	}

	@Test
	public void test31() {
		Timer timer = new Timer();
		timer.start();
		runAndCheck("test/run_31.bds", "events", "[runnning, kill, done]");
		Assert.assertTrue(timer.elapsed() < 1 * 1000); // We should finish in much less than 1 secs (the program waits 60secs)
	}

	@Test
	public void test32() {
		runAndCheck("test/run_32.bds", "out", "Hi\n");
	}

	@Test
	public void test33() {
		runAndCheck("test/run_33.bds", "err", "Hi\n");
	}

	@Test
	public void test34() {
		runAndCheck("test/run_34.bds", "exitStat", "0");
	}

	@Test
	public void test35() {
		runAndCheck("test/run_35.bds", "exitStat", "1");
	}

	@Test
	public void test36() {
		runAndCheck("test/run_36.bds", "s", "before");
	}

	@Test
	public void test37() {
		runAndCheck("test/run_37.bds", "s", "after");
	}

	@Test
	public void test38() {
		runAndCheck("test/run_38.bds", "su", "$s world \\n");
	}

	@Test
	public void test39() {
		String home = System.getenv("HOME");
		runAndCheck("test/run_39.bds", "home", home);
	}

	@Test
	public void test40() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("file", "zzz.txt");
		expectedValues.put("opt", "true");
		expectedValues.put("num", "42");
		expectedValues.put("rnum", "3.1415");
		expectedValues.put("args", "[-file, zzz.txt, -num, 42, -rnum, 3.1415, -opt, -notProcessed, more, arguments]");

		ArrayList<String> args = new ArrayList<String>();

		args.add("-file");
		args.add("zzz.txt");

		args.add("-num");
		args.add("42");

		args.add("-rnum");
		args.add("3.1415");

		args.add("-opt");

		args.add("-notProcessed");
		args.add("more");
		args.add("arguments");

		runAndCheckMultiple("test/run_40.bds", expectedValues, args);
	}

	@Test
	public void test41() {
		runAndCheck("test/run_01.bds", "programName", "run_01.bds");
	}

	@Test
	public void test42() {
		runAndCheck("test/run_42.bds", "i", 6L);
	}

	@Test
	public void test43() {
		runAndCheck("test/run_43.bds", "finished", 0L);
	}

	public void test44() {
		runAndCheckExit("test/run_44.bds", 0);
	}

	public void test45() {
		runAndCheckExit("test/run_45.bds", 1);
	}

	public void test46() {
		runAndCheck("test/run_46.bds", "i", 2L);
	}

	@Test
	public void test47() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("in", "[in1.txt, in2.txt, in3.txt]");
		expectedValues.put("out", "zzz.txt");
		expectedValues.put("ok", "true");

		ArrayList<String> args = new ArrayList<String>();

		args.add("-ok");

		args.add("-in");
		args.add("in1.txt");
		args.add("in2.txt");
		args.add("in3.txt");

		args.add("-out");
		args.add("zzz.txt");

		runAndCheckMultiple("test/run_47.bds", expectedValues, args);
	}

	@Test
	public void test48() {
		runAndCheck("test/run_48.bds", "step", 2L);
	}

	@Test
	public void test50() {
		runAndCheck("test/run_50.bds", "j", 302);
		runAndCheck("test/run_50.bds", "i", 32);
		runAndCheck("test/run_50.bds", "jx", 44);
	}

	@Test
	public void test51() {
		runAndCheck("test/run_51.bds", "hash", "{hi=bye}");
	}

	@Test
	public void test52() {
		runAndCheck("test/run_52.bds", "hash", "{one=1}");
	}

	@Test
	public void test53() {
		runAndCheck("test/run_53.bds", "vals", "[bye, chau]");
	}

	@Test
	public void test54() {
		runAndCheck("test/run_54.bds", "vals", "[hi, hola]");
	}

	@Test
	public void test55() {
		runAndCheck("test/run_55.bds", "hk1", "true");
		runAndCheck("test/run_55.bds", "hk2", "false");
		runAndCheck("test/run_55.bds", "hv1", "true");
		runAndCheck("test/run_55.bds", "hv2", "false");
		runAndCheck("test/run_55.bds", "hk3", "false");
	}

}
