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
	public void test13_checkPoint_function_with_empty_Args() {
		runAndCheckpoint("test/checkpoint_13.bds", "test/checkpoint_13.chp", "ok", "true");
	}
}
