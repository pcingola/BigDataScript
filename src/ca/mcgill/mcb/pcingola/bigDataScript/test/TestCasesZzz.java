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

}
