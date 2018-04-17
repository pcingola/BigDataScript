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

	//	@Test
	//	public void test38() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		runAndCheck("test/run_38.bds", "su", "$s world \\n");
	//	}
	//
	//	@Test
	//	public void test54() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		runAndCheck("test/run_54.bds", "vals", "[hi, hola]");
	//	}
	//
	//	@Test
	//	public void test65() {
	//		Gpr.debug("Test");
	//		verbose = true;
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
	//	public void test95() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_95.bds", "ll", "[zero, one, two, three, four, 5]");
	//	}

	//	@Test
	//	public void test97() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		HashMap<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("h1", "1");
	//		expectedValues.put("h2", "3");
	//		expectedValues.put("h3", "2");
	//		expectedValues.put("h4", "2");
	//		expectedValues.put("h5", "1");
	//		runAndCheck("test/run_97.bds", expectedValues);
	//	}

	@Test
	public void test98() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("h1", "1");
		expectedValues.put("h2", "3");
		expectedValues.put("h3", "2");
		expectedValues.put("h4", "2");
		expectedValues.put("h5", "1");
		runAndCheck("test/run_98.bds", expectedValues);
	}

}
