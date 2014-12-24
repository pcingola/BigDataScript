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
	public void test17_checkpoint_listIndex() {
		runAndCheckpoint("test/z.bds", "test/z.chp", "res", "34");
	}

}
