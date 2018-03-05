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
	public void test53() {
		Gpr.debug("Test");
		runAndCheck("test/run_53.bds", "vals", "[bye, chau]");
	}

	//	@Test
	//	public void test54() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_54.bds", "vals", "[hi, hola]");
	//	}
	//
	//	@Test
	//	public void test55() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_55.bds", "hk1", "true");
	//		runAndCheck("test/run_55.bds", "hk2", "false");
	//		runAndCheck("test/run_55.bds", "hv1", "true");
	//		runAndCheck("test/run_55.bds", "hv2", "false");
	//		runAndCheck("test/run_55.bds", "hk3", "false");
	//	}
	//
	//	@Test
	//	public void test56() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_56.bds", "out", "Adios;Au revoir;Bye;");
	//		runAndCheck("test/run_56.bds", "str", "map = { Bonjour => Au revoir, Hello => Bye, Hola => Adios }");
	//	}
	//
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
	//	public void test76() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_76.bds", "list", "[0, 2, 4, 6, 8, 10]");
	//	}
	//
	//	@Test
	//	public void test92() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_92.bds", "outs", "TASK 1\nTASK 2\n");
	//	}
	//
	//	@Test
	//	public void test93() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_93.bds", "outs", "TASK 1\nTASK 2\n");
	//	}

}
