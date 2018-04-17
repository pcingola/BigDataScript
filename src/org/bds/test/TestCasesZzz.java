package org.bds.test;

import org.bds.util.Gpr;
import org.bds.util.Timer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test31() {
		Gpr.debug("Test");
		verbose = true;
		Timer timer = new Timer();
		timer.start();
		runAndCheck(1, "test/run_31.bds", "events", "[runnning, kill, done]");
		Assert.assertTrue(timer.elapsed() < 1 * 1000); // We should finish in much less than 1 secs (the program waits 60secs)
	}

	//	@Test
	//	public void test38() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_38.bds", "su", "$s world \\n");
	//	}
	//
	//	@Test
	//	public void test40() {
	//		Gpr.debug("Test");
	//		HashMap<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("file", "zzz.txt");
	//		expectedValues.put("opt", "true");
	//		expectedValues.put("num", "42");
	//		expectedValues.put("rnum", "3.1415");
	//		expectedValues.put("args", "[-file, zzz.txt, -num, 42, -rnum, 3.1415, -opt, -notProcessed, more, arguments]");
	//
	//		// Arguments to add after program name
	//		ArrayList<String> argsAfter = new ArrayList<>();
	//
	//		argsAfter.add("-file");
	//		argsAfter.add("zzz.txt");
	//
	//		argsAfter.add("-num");
	//		argsAfter.add("42");
	//
	//		argsAfter.add("-rnum");
	//		argsAfter.add("3.1415");
	//
	//		argsAfter.add("-opt");
	//
	//		argsAfter.add("-notProcessed");
	//		argsAfter.add("more");
	//		argsAfter.add("arguments");
	//
	//		runAndCheck("test/run_40.bds", expectedValues, argsAfter);
	//	}

}
