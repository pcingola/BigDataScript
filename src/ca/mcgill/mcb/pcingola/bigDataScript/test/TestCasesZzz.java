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
	public void test07() {

		// TO CHECK:
		// 		i) 2 Tasks created
		//		ii) checkpoint saved
		//		iii) run from checkpoint
		//		iv) make sure task dependencies remains unchanged (executed one after the other)

		//runAndCheckpoint("test/graph_04.bds", "test/graph_04.chp", "out", "Task start\nTask end\n");
		runAndCheck("test/z.bds", "output", "Task start\nTask end\n");
	}

}
