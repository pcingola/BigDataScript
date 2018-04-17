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

	@Test
	public void test68() {
		Gpr.debug("Test");
		runAndCheck("test/run_68.bds", "out", "hi bye end\n");
	}

	//	@Test
	//	public void test74() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_74.bds", "i", 10L);
	//	}
	//
	//	@Test
	//	public void test75() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_75.bds", "ls", "EXEC\ntest/run_75.bds\nDONE\n");
	//	}
	//
	//	@Test
	//	public void test92() {
	//		Gpr.debug("Test");
	//		runAndCheck("test/run_92.bds", "outs", "TASK 1\nTASK 2\n");
	//	}

}
