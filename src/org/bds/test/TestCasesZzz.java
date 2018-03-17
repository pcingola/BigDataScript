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

	//	@Test
	//	public void test200() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_200.bds", "", "");
	//	}

	//	@Test
	//	public void test201() {
	//		Gpr.debug("Test");
	//		Map<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("z", "{ i: 0, r: 0.0, s: \"\" }");
	//		expectedValues.put("z2", "{ i: 0, r: 0.0, s: \"\" }");
	//
	//		runAndCheck("test/run_201.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void test202() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_202.bds", "z", "{ i: 42, r: 1.234, s: \"Hi\" }");
	//	}
	//
	//	@Test
	//	public void test203() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_203.bds", "j", "42");
	//	}
	//
	//	@Test
	//	public void test204() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_204.bds", "j", "42");
	//	}
	//
	//	@Test
	//	public void test205() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_205.bds", "z", "null");
	//	}
	//
	//	@Test
	//	public void test206() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		runAndCheck("test/run_206.bds", "j", "44");
	//	}
	//
	//	@Test
	//	public void test207() {
	//		Gpr.debug("Test");
	//		Map<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("j", "42");
	//		expectedValues.put("s", "bye");
	//		expectedValues.put("s2", "chau");
	//
	//		runAndCheck("test/run_207.bds", expectedValues);
	//	}
	//
	//	@Test
	//	public void test208() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_208.bds", "z", "{ i: 7, l: [one, dos, three], m: { one => uno,  three => tres,  two => deux } }");
	//	}

	@Test
	public void test209() {
		Gpr.debug("Test");
		runAndCheckStderr("test/run_209.bds", "Null pointer: Cannot access field 'i' in object type 'Zzz'");
	}

	@Test
	public void test210() {
		Gpr.debug("Test");
		runAndCheckStderr("test/run_210.bds", "Null pointer: Cannot CALL METHOD...'");
	}

	@Test
	public void test211() {
		Gpr.debug("Test");
		runAndCheck("test/run_211.bds", "", "");
	}

	@Test
	public void test212() {
		Gpr.debug("Test");
		runAndCheck("test/run_212.bds", "", "");
	}

	@Test
	public void test213() {
		Gpr.debug("Test");
		runAndCheck("test/run_213.bds", "", "");
	}

}
