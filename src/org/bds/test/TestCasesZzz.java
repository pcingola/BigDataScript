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
	public void test22() {
		Gpr.debug("Test");
		runAndCheck("test/run_22.bds", "l2", "file_3.txt");
	}

	@Test
	public void test28() {
		Gpr.debug("Test");
		verbose = true;
		runAndCheckpoint("test/checkpoint_28.bds", "test/checkpoint_28.chp", "out", 47);
	}

}
