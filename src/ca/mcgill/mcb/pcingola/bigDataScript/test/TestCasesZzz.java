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
	public void test08() {
		runAndCheckStdout("test/graph_08.bds", "MID1\nMID2\nOUT");
	}

}
