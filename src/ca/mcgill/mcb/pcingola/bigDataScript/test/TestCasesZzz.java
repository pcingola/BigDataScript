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
	public void test01() {
		runAndCheckpoint("test/checkpoint_01.bds", null, "i", "10");
	}

	@Test
	public void test02() {
		runAndCheckpoint("test/checkpoint_02.bds", null, "l", "15");
	}

}
