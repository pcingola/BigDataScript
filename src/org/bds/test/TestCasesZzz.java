package org.bds.test;

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
	public void test64() {
		Gpr.debug("Test");
		runAndCheck("test/run_64.bds", "m", "{}");
	}

	@Test
	public void test65() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("bsfalse", false);
		expectedValues.put("bstrue", true);
		expectedValues.put("bifalse", false);
		expectedValues.put("bitrue", true);
		expectedValues.put("brfalse", false);
		expectedValues.put("brtrue", true);
		expectedValues.put("blfalse", false);
		expectedValues.put("bltrue", true);
		expectedValues.put("bmfalse", false);
		expectedValues.put("bmtrue", true);
		runAndCheck("test/run_65.bds", expectedValues);
	}

	@Test
	public void test66() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("sif", "String 'hi' is NOT empty");
		expectedValues.put("lif", "List '[hi, bye]' is NOT empty");
		expectedValues.put("mif", "Map '{  }' IS empty");
		runAndCheck("test/run_66.bds", expectedValues);
	}

	@Test
	public void test92() {
		Gpr.debug("Test");
		runAndCheck("test/run_92.bds", "outs", "TASK 1\nTASK 2\n");
	}

	@Test
	public void test93() {
		Gpr.debug("Test");
		runAndCheck("test/run_93.bds", "outs", "TASK 1\nTASK 2\n");
	}

}
