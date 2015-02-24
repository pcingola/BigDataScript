package ca.mcgill.mcb.pcingola.bigDataScript.test;

import junit.framework.Assert;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

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

		String expectedOutput = "IN: /Users/pcingola/zzz/in.txt\n" //
				+ "OUT: /Users/pcingola/zzz/out.txt\n" //
				+ "OUT_0: /Users/pcingola/zzz/out_0.txt\n" //
				+ "OUT_1: /Users/pcingola/zzz/out_1.txt\n" //
				+ "OUT_2: /Users/pcingola/zzz/out_2.txt\n" //
				+ "    OUT_0_0: /Users/pcingola/zzz/out_0_0.txt\n" //
				+ "    OUT_0_1: /Users/pcingola/zzz/out_0_1.txt\n" //
				+ "    OUT_0_2: /Users/pcingola/zzz/out_0_2.txt\n" //
				+ "    OUT_1_0: /Users/pcingola/zzz/out_1_0.txt\n" //
				+ "    OUT_1_1: /Users/pcingola/zzz/out_1_1.txt\n" //
				+ "    OUT_1_2: /Users/pcingola/zzz/out_1_2.txt\n" //
				+ "    OUT_2_0: /Users/pcingola/zzz/out_2_0.txt\n" //
				+ "    OUT_2_1: /Users/pcingola/zzz/out_2_1.txt\n" //
				+ "    OUT_2_2: /Users/pcingola/zzz/out_2_2.txt\n" //
		;

		String stdout = runAndReturnStdout("test/run_126.bds");
		if (verbose) System.err.println("STDOUT: " + stdout);

		// Check that task output lines
		for (String out : expectedOutput.split("\n")) {
			Assert.assertTrue("Expected output line not found: '" + out + "'", stdout.contains(out));
		}
	}

}
