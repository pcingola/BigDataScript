package org.bds.test;

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
	public void test58() {
		Gpr.debug("Test");
		verbose = true;
		runAndCheck("test/run_58.bds", "z", 0L);
	}

	//	@Test
	//	public void test63() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_63.bds", "l", "[]");
	//	}
	//
	//	@Test
	//	public void test64() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_64.bds", "m", "{}");
	//	}
	//
	//	@Test
	//	public void test65() {
	//		Gpr.debug("Test");
	//		HashMap<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("bsfalse", false);
	//		expectedValues.put("bstrue", true);
	//		expectedValues.put("bifalse", false);
	//		expectedValues.put("bitrue", true);
	//		expectedValues.put("brfalse", false);
	//		expectedValues.put("brtrue", true);
	//		expectedValues.put("blfalse", false);
	//		expectedValues.put("bltrue", true);
	//		expectedValues.put("bmfalse", false);
	//		expectedValues.put("bmtrue", true);
	//		runAndCheck("test/run_65.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void test66() {
	//		Gpr.debug("Test");
	//		HashMap<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("sif", "String 'hi' is NOT empty");
	//		expectedValues.put("lif", "List '[hi, bye]' is NOT empty");
	//		expectedValues.put("mif", "Map '{  }' IS empty");
	//		runAndCheck("test/run_66.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void test67() {
	//		Gpr.debug("Test");
	//		HashMap<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("s", "varS");
	//		expectedValues.put("s1", "Hi '$'");
	//		expectedValues.put("s2", "Hi $");
	//		expectedValues.put("s3", "Hi $ bye");
	//		runAndCheck("test/run_67.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void test68() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_68.bds", "out", "hi bye end\n");
	//	}
	//
	//	@Test
	//	public void test70() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_70.bds", "i", 10L);
	//	}
	//
	//	@Test
	//	public void test71() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_71.bds", "i", 10L);
	//	}
	//
	//	@Test
	//	public void test72() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_72.bds", "i", 10L);
	//	}

}
