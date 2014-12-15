package ca.mcgill.mcb.pcingola.bigDataScript.test;

import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test117_serial_parallel_tasks() {
		String expectedStdout = "Iter 1, Task 1: End\n" //
				+ "Iter 1, Task 2: Start\n" //
				+ "Iter 1, Task 2: End\n" //
				+ "Iter 5, Task 1: End\n" //
				+ "Iter 5, Task 2: Start\n" //
				+ "Iter 5, Task 2: End\n" //
		;;

		String stdout = runAndReturnStdout("test/run_117.bds");

		if (stdout.indexOf(expectedStdout) < 0) {
			String msg = "Cannot find desired output:\n" //
					+ "---------- Expected output ----------\n" //
					+ expectedStdout //
					+ "-------------- STDOUT --------------\n" //
					+ stdout //
			;
			System.err.println(msg);
			throw new RuntimeException(msg);
		}
	}

}
