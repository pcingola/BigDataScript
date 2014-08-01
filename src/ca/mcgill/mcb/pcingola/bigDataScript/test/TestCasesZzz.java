package ca.mcgill.mcb.pcingola.bigDataScript.test;

import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	//	@Test
	//	public void test02() {
	//		runAndCheck("test/graph_02.bds", "output", "IN\nTASK 1\nTASK 2\nTASK 3\n");
	//	}

	@Test
	public void test09() {
		runAndCheckStderr("test/graph_09.bds", "Circular dependency");
	}

}
