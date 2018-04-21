package org.bds.test;

import java.util.HashMap;
import java.util.Map;

import org.bds.util.Gpr;
import org.junit.Test;

/**
 * Test cases Classes / Objects
 *
 * @author pcingola
 *
 */
public class TestCasesRun3 extends TestCasesBase {

	//	@Test
	//	public void test200() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_200.bds", "", "");
	//	}

	@Test
	public void test201() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("z", "{ i: 0, r: 0.0, s:  }");
		expectedValues.put("z2", "{ i: 0, r: 0.0, s:  }");

		runAndCheck("test/run_201.bds", expectedValues);
	}

	@Test
	public void test202() {
		Gpr.debug("Test");
		runAndCheck("test/run_202.bds", "z", "{ i: 42, r: 1.234, s: Hi }");
	}

	@Test
	public void test203() {
		Gpr.debug("Test");
		runAndCheck("test/run_203.bds", "j", "42");
	}

	@Test
	public void test204() {
		Gpr.debug("Test");
		runAndCheck("test/run_204.bds", "j", "42");
	}

	@Test
	public void test205() {
		Gpr.debug("Test");
		runAndCheck("test/run_205.bds", "z", "null");
	}

	@Test
	public void test206() {
		Gpr.debug("Test");
		runAndCheck("test/run_206.bds", "j", "44");
	}

	@Test
	public void test207() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("j", "42");
		expectedValues.put("s", "bye");
		expectedValues.put("s2", "chau");
		runAndCheck("test/run_207.bds", expectedValues);
	}

	@Test
	public void test208() {
		Gpr.debug("Test");
		runAndCheck("test/run_208.bds", "z", "{ i: 7, l: [one, dos, three], m: { one => uno, three => tres, two => deux } }");
	}

	@Test
	public void test209() {
		Gpr.debug("Test");
		verbose = true;
		runAndCheckStderr("test/run_209.bds", "Null pointer: Cannot access field 'i' from null object.");
	}

	@Test
	public void test210() {
		Gpr.debug("Test");
		runAndCheckStderr("test/run_210.bds", "Null pointer: Cannot call method 'Zzz.set' in null object");
	}

	@Test
	public void test211() {
		Gpr.debug("Test");
		runAndCheck("test/run_211.bds", "z", "{ i: 7 }");
	}

	@Test
	public void test212() {
		Gpr.debug("Test");
		runAndCheck("test/run_212.bds", "z", "{ i: 42 }");
	}

	@Test
	public void test213() {
		Gpr.debug("Test");
		runAndCheck("test/run_213.bds", "z", "{ i: 7 }");
	}

	@Test
	public void test214() {
		Gpr.debug("Test");
		runAndCheck("test/run_214.bds", "z", "{ i: 42 }");
	}

	@Test
	public void test215() {
		Gpr.debug("Test");
		runAndCheck("test/run_215.bds", "z", "{ i: 42, j: 7 }");
	}

	@Test
	public void test216() {
		Gpr.debug("Test");
		runAndCheck("test/run_216.bds", "z", "{ i: 21, j: 17, next: { i: 42, next: null } }");
	}

	@Test
	public void test217() {
		Gpr.debug("Test");
		runAndCheck("test/run_217.bds", "x", "43");
	}

	@Test
	public void test218() {
		Gpr.debug("Test");
		runAndCheck("test/run_218.bds", "x", "50");
	}

	@Test
	public void test219() {
		Gpr.debug("Test");
		runAndCheck("test/run_219.bds", "x", "50");
	}

	@Test
	public void test220() {
		Gpr.debug("Test");
		runAndCheck("test/run_220.bds", "x", "50");
	}

}
