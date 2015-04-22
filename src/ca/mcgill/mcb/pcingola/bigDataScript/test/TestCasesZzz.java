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
	public void test13_goal_using_taskId() {
		runAndCheckStdout("test/graph_13.bds", "out1_2.txt\nout2_1.txt");
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
	//				+ "sys                |\\t|\n" //
	//				+ "sys                |\\t|    variable:Hello\n" //
	//				+ "task               |\\t|\n" //
	//				+ "task               |\\t|    variable:Hello\n" //
	//		;
	//
	//		runAndCheckStdout("test/run_123.bds", output);
	//	}
	//
	//	@Test
	//	public void test08() {
	//		runAndCheckStdout("test/graph_08.bds", "MID1\nMID2\nOUT");
	//	}
	//
	//	@Test
	//	public void test119_task_dependency() {
	//		Gpr.debug("Test");
	//
	//		// Delete input file
	//		String inFile = "tmp_in.txt";
	//		(new File(inFile)).delete();
	//
	//		String expectedStdout1 = "Creating tmp_in.txt\n" //
	//				+ "Running task\n" //
	//				+ "Creating tmp_out.txt\n" //
	//				+ "Done\n" //"
	//		;
	//
	//		String expectedStdout2 = "Running task\n" //
	//				+ "Done\n" //"
	//		;
	//
	//		System.out.println("First run:");
	//		runAndCheckStdout("test/run_119.bds", expectedStdout1);
	//
	//		System.out.println("\n\nSecond run:");
	//		runAndCheckStdout("test/run_119.bds", expectedStdout2);
	//	}
	//
}
