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
	public void test12() {
		// Run pipeline and test checkpoint
		runAndCheckpoint("test/checkpoint_12.bds", "test/checkpoint_12.chp", "ok", "true");
	}
}
