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
	public void test14_dep_using_taskId() {
		runAndCheckStdout("test/graph_14.bds", "Hello\nBye");
	}

}
