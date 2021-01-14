package org.bds.test.unit;

import java.util.HashMap;
import java.util.Map;

import org.bds.test.TestCasesBase;
import org.bds.util.Gpr;
import org.junit.Test;

/**
 * Test cases Classes / Objects
 *
 * @author pcingola
 *
 */
public class TestCasesRun3 extends TestCasesBase {

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
		runAndCheck("test/run_205.bds", "z", null);
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
		runAndCheckStderr("test/run_209.bds", "Null pointer. Trying to access field 'i' in null object.");
	}

	@Test
	public void test210() {
		Gpr.debug("Test");
		runAndCheckStderr("test/run_210.bds", "Null pointer: Cannot call method 'Zzz.set(Zzz,int) -> void' on null object.");
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

	@Test
	public void test221() {
		Gpr.debug("Test");
		runAndCheck("test/run_221.bds", "z", "46");
	}

	@Test
	public void test222() {
		Gpr.debug("Test");
		runAndCheck("test/run_222.bds", "z", "7");
	}

	@Test
	public void test223_list_of_list() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("l", "[[hi, bye], [hola, adios]]");
		expectedValues.put("typel", "string[][]");
		expectedValues.put("typel0", "string[]");

		runAndCheck("test/run_223.bds", expectedValues);
	}

	@Test
	public void test224_map_of_lists() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("m", "{ en => [hi, bye, hello], sp => [hola, adios] }");
		expectedValues.put("typem", "string[]{string}");
		expectedValues.put("typemen", "string[]");

		runAndCheck("test/run_224.bds", expectedValues);
	}

	@Test
	public void test225_super() {
		Gpr.debug("Test");
		runAndCheck("test/run_225.bds", "ret", "2");
	}

	@Test
	public void test226_refref() {
		Gpr.debug("Test");
		runAndCheck("test/run_226.bds", "ret", "42");
	}

	@Test
	public void test227_refref() {
		Gpr.debug("Test");
		runAndCheck("test/run_227.bds", "ret", "42");
	}

	@Test
	public void test228_method_call() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("ret1", "1");
		expectedValues.put("ret2", "2");

		runAndCheck("test/run_228.bds", expectedValues);
	}

	@Test
	public void test229_super() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("af", "1");
		expectedValues.put("ag", "2");
		expectedValues.put("ax", "41");
		expectedValues.put("bf", "11");
		expectedValues.put("bg", "12");
		expectedValues.put("bx", "42");

		runAndCheck("test/run_229.bds", expectedValues);
	}

	@Test
	public void test230_tryCatch() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("try1", "true");
		expectedValues.put("catch1", "false");
		expectedValues.put("finally1", "true");

		runAndCheck("test/run_230.bds", expectedValues);
	}

	@Test
	public void test231_tryCatch() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("try1", "true");
		expectedValues.put("catch1", "true");
		expectedValues.put("finally1", "true");

		runAndCheck("test/run_231.bds", expectedValues);
	}

	@Test
	public void test232_tryCatch() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("f1", "true");
		expectedValues.put("f2", "false");
		expectedValues.put("try1", "true");
		expectedValues.put("try2", "false");
		expectedValues.put("catch1", "true");
		expectedValues.put("finally1", "true");

		runAndCheck("test/run_232.bds", expectedValues);
	}

	@Test
	public void test233_tryCatch() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("f1", "true");
		expectedValues.put("f2", "true");
		expectedValues.put("try1", "true");
		expectedValues.put("try2", "false");
		expectedValues.put("catch1", "true");
		expectedValues.put("finally1", "true");

		runAndCheck("test/run_233.bds", expectedValues);
	}

	@Test
	public void test234_tryCatch() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("try11", "true");
		expectedValues.put("try12", "false");
		expectedValues.put("catch11", "true");
		expectedValues.put("finally11", "true");

		expectedValues.put("try21", "true");
		expectedValues.put("try22", "true");
		expectedValues.put("catch21", "false");
		expectedValues.put("finally21", "true");

		runAndCheck("test/run_234.bds", expectedValues);
	}

	@Test
	public void test235_tryCatch() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("f11", "true");
		expectedValues.put("f12", "false");
		expectedValues.put("f21", "true");
		expectedValues.put("f22", "false");

		expectedValues.put("try11", "true");
		expectedValues.put("try12", "false");
		expectedValues.put("catch11", "false");
		expectedValues.put("finally11", "true");

		expectedValues.put("try21", "true");
		expectedValues.put("try22", "false");
		expectedValues.put("catch21", "true");
		expectedValues.put("finally21", "true");

		runAndCheck("test/run_235.bds", expectedValues);
	}

	@Test
	public void test236_tryCatch() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("f11", "true");
		expectedValues.put("f12", "false");
		expectedValues.put("f21", "true");
		expectedValues.put("f22", "false");

		expectedValues.put("try11", "true");
		expectedValues.put("try12", "false");
		expectedValues.put("catch11", "true");
		expectedValues.put("finally11", "true");

		expectedValues.put("try21", "true");
		expectedValues.put("try22", "false");
		expectedValues.put("catch21", "true");
		expectedValues.put("finally21", "true");

		runAndCheck("test/run_236.bds", expectedValues);
	}

	@Test
	public void test237_tryCatch() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("main1", "true");
		expectedValues.put("main2", "true");

		expectedValues.put("try1", "true");
		expectedValues.put("try2", "false");
		expectedValues.put("catch1", "true");

		runAndCheck("test/run_237.bds", expectedValues);
	}

	@Test
	public void test238_tryCatch() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("main1", "true");
		expectedValues.put("main2", "false");

		expectedValues.put("try1", "true");
		expectedValues.put("try2", "false");
		expectedValues.put("finally1", "true");
		expectedValues.put("finally2", "true");

		runAndCheck(1, "test/run_238.bds", expectedValues);
	}

	@Test
	public void test239_tryCatch() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("main1", "true");
		expectedValues.put("main2", "false");

		expectedValues.put("try1", "true");
		expectedValues.put("try2", "false");
		expectedValues.put("finally1", "true");
		expectedValues.put("finally2", "false");

		runAndCheck(1, "test/run_239.bds", expectedValues);
	}

	@Test
	public void test240_tryCatch() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("main1", "true");
		expectedValues.put("main2", "false");

		expectedValues.put("try1", "true");
		expectedValues.put("try2", "false");
		expectedValues.put("finally1", "true");
		expectedValues.put("finally2", "false");

		runAndCheck(1, "test/run_240.bds", expectedValues);
	}

	@Test
	public void test241_tryCatch() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("main1", "true");
		expectedValues.put("main2", "false");
		expectedValues.put("g1", "true");
		expectedValues.put("g2", "false");

		expectedValues.put("try1", "true");
		expectedValues.put("try2", "false");
		expectedValues.put("finally1", "true");
		expectedValues.put("finally2", "false");

		runAndCheck(1, "test/run_241.bds", expectedValues);
	}

	@Test
	public void test242_derivedMethodParamNames() {
		Gpr.debug("Test");
		runAndCheck("test/run_242.bds", "ret", "n:hi");
	}

	@Test
	public void test243_downCasting() {
		runAndCheck("test/run_243.bds", "ret", "42");
	}

	@Test
	public void test244_concurrent_modification() {
		runAndCheckException("test/run_244.bds", "ConcurrentModificationException");
	}

	@Test
	public void test245_out_of_bounds() {
		runAndCheck("test/run_245.bds", "ret", "5");
	}

	@Test
	public void test246_out_of_bounds() {
		runAndCheck("test/run_246.bds", "ret", "1");
	}

	@Test
	public void test247_out_of_bounds() {
		runAndCheck("test/run_247.bds", "ret", "5");
	}

	@Test
	public void test248_out_of_bounds() {
		runAndCheck("test/run_248.bds", "ret", "1");
	}

	@Test
	public void test249_concurrent_modification_hash() {
		runAndCheckException("test/run_249.bds", "ConcurrentModificationException");
	}

	@Test
	public void test250_super_super_method_call() {
		runAndCheckStdout("test/run_250.bds", "GrandParent\nParent\nChild\n");
	}

	@Test
	public void test251_super_super_constructor_call() {
		runAndCheckStdout("test/run_251.bds", "GrandParent\nParent\nChild\n");
	}

	@Test
	public void test253_getvar() {
		HashMap<String, Object> expectedValues = new HashMap<>();

		expectedValues.put("shome", System.getenv().get("HOME"));
		expectedValues.put("bhome", true);

		expectedValues.put("szzz", "");
		expectedValues.put("bzzz", false);

		runAndCheck("test/run_253.bds", expectedValues);
	}

	@Test
	public void test254_getvar() {
		HashMap<String, Object> expectedValues = new HashMap<>();

		expectedValues.put("shome", System.getenv().get("HOME"));
		expectedValues.put("szzz", "VALUE_DEFAULT_2");
		expectedValues.put("szzzxxxzzz", "VALUE_DEFAULT_3");

		runAndCheck("test/run_254.bds", expectedValues);
	}

	@Test
	public void test255_getModuleName() {
		Gpr.debug("Test");
		runAndCheck("test/run_255.bds", "b", "run_255.bds");
	}

	@Test
	public void test256_getModuleName() {
		Gpr.debug("Test");
		runAndCheck("test/run_256.bds", "db", "test");
	}

	@Test
	public void test257_randIntDivisionByZero() {
		Gpr.debug("Test");
		runAndCheck("test/run_257.bds", "r", "0");
	}

}
