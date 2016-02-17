package org.bds.test;

import org.bds.util.Gpr;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	/**
	 * Task dependent on output from a scheduled task
	 */
	@Test
	public void test126_task_dependency_scheduled() {
		Gpr.debug("Test");

		String expectedOutput = "IN: " + Gpr.HOME + "/zzz/in.txt\n" //
				+ "OUT: " + Gpr.HOME + "/zzz/out.txt\n" //
				+ "OUT_0: " + Gpr.HOME + "/zzz/out_0.txt\n" //
				+ "    OUT_0_0: " + Gpr.HOME + "/zzz/out_0_0.txt\n" //
				+ "    OUT_0_1: " + Gpr.HOME + "/zzz/out_0_1.txt\n" //
				+ "    OUT_0_2: " + Gpr.HOME + "/zzz/out_0_2.txt\n" //
				+ "OUT_1: " + Gpr.HOME + "/zzz/out_1.txt\n" //
				+ "    OUT_1_0: " + Gpr.HOME + "/zzz/out_1_0.txt\n" //
				+ "    OUT_1_1: " + Gpr.HOME + "/zzz/out_1_1.txt\n" //
				+ "    OUT_1_2: " + Gpr.HOME + "/zzz/out_1_2.txt\n" //
				+ "OUT_2: " + Gpr.HOME + "/zzz/out_2.txt\n" //
				+ "    OUT_2_0: " + Gpr.HOME + "/zzz/out_2_0.txt\n" //
				+ "    OUT_2_1: " + Gpr.HOME + "/zzz/out_2_1.txt\n" //
				+ "    OUT_2_2: " + Gpr.HOME + "/zzz/out_2_2.txt\n" //
				;

		String stdout = runAndReturnStdout("test/run_126.bds");
		if (verbose) {
			System.err.println("STDOUT:" //
					+ "\n----------------------------------------\n" //
					+ stdout //
					+ "\n----------------------------------------" //
			);
		}

		// Check that task output lines
		for (String out : expectedOutput.split("\n")) {
			Assert.assertTrue("Expected output line not found: '" + out + "'", stdout.contains(out));
		}
	}

}
