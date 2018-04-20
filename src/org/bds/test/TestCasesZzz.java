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
	//	public void test114_parallel_function_task_calls() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		String stdout = runAndReturnStdout("test/run_114.bds");
	//
	//		Set<String> linesPar = new HashSet<>();
	//		for (String line : stdout.split("\n"))
	//			if (line.startsWith("TASK")) linesPar.add(line);
	//
	//		// Check
	//		Assert.assertTrue("There should be 5 tasks", linesPar.size() == 5);
	//	}

	@Test
	public void test122_nestest_break_continue() {
		Gpr.debug("Test");
		runAndCheck("test/run_122.bds", "out", "5\t7");
	}

	//	@Test
	//	public void test123_literals_sys_task() {
	//		Gpr.debug("Test");
	//
	//		String output = "print_quote        |\\t|\n" //
	//				+ "print_quote        |\\t|    variable:$hi\n" //
	//				+ "print_double       |\t|\n" //
	//				+ "print_double       |\t|    variable:Hello\n" //
	//				+ "print_double_esc   |\\t|\n" //
	//				+ "print_double_esc   |\\t|   variable:Hello\n" //
	//				// Note: This result may change if we use a different sysShell in bds.config
	//				+ "sys                |\\t|\n" //
	//				+ "sys                |\\t|    variable:Hello\n" //
	//				// Note: This result may change if we use a different taskShell in bds.config
	//				+ "task               |\\t|\n" //
	//				+ "task               |\\t|    variable:Hello\n" //
	//		;
	//
	//		runAndCheckStdout("test/run_123.bds", output);
	//	}
	//
	//	/**
	//	 * Task dependent on output from a scheduled task
	//	 */
	//	@Test
	//	public void test126_task_dependency_scheduled() {
	//		Gpr.debug("Test");
	//
	//		String expectedOutput = "IN: " + Gpr.HOME + "/zzz/in.txt\n" //
	//				+ "OUT: " + Gpr.HOME + "/zzz/out.txt\n" //
	//				+ "OUT_0: " + Gpr.HOME + "/zzz/out_0.txt\n" //
	//				+ "    OUT_0_0: " + Gpr.HOME + "/zzz/out_0_0.txt\n" //
	//				+ "    OUT_0_1: " + Gpr.HOME + "/zzz/out_0_1.txt\n" //
	//				+ "    OUT_0_2: " + Gpr.HOME + "/zzz/out_0_2.txt\n" //
	//				+ "OUT_1: " + Gpr.HOME + "/zzz/out_1.txt\n" //
	//				+ "    OUT_1_0: " + Gpr.HOME + "/zzz/out_1_0.txt\n" //
	//				+ "    OUT_1_1: " + Gpr.HOME + "/zzz/out_1_1.txt\n" //
	//				+ "    OUT_1_2: " + Gpr.HOME + "/zzz/out_1_2.txt\n" //
	//				+ "OUT_2: " + Gpr.HOME + "/zzz/out_2.txt\n" //
	//				+ "    OUT_2_0: " + Gpr.HOME + "/zzz/out_2_0.txt\n" //
	//				+ "    OUT_2_1: " + Gpr.HOME + "/zzz/out_2_1.txt\n" //
	//				+ "    OUT_2_2: " + Gpr.HOME + "/zzz/out_2_2.txt\n" //
	//		;
	//
	//		String stdout = runAndReturnStdout("test/run_126.bds");
	//		if (verbose) {
	//			System.err.println("STDOUT:" //
	//					+ "\n----------------------------------------\n" //
	//					+ stdout //
	//					+ "\n----------------------------------------" //
	//			);
	//		}
	//
	//		// Check that task output lines
	//		for (String out : expectedOutput.split("\n")) {
	//			Assert.assertTrue("Expected output line not found: '" + out + "'", stdout.contains(out));
	//		}
	//	}
	//
	//	@Test
	//	public void test136() {
	//		Gpr.debug("Test");
	//		HashMap<String, Object> expectedValues = new HashMap<>();
	//		expectedValues.put("l", "[1, 99, 2, 3]");
	//		expectedValues.put("l2", "[3, 2, 99, 1]");
	//		expectedValues.put("l3", "[3, 2, 99, 1, 99]");
	//		expectedValues.put("l3count", "2");
	//		expectedValues.put("l3idx", "2");
	//		expectedValues.put("l4", "[3, 2, 1, 99]");
	//		expectedValues.put("l5", "[3, 2, 1, 99]");
	//
	//		runAndCheck("test/run_136.bds", expectedValues);
	//	}

}
