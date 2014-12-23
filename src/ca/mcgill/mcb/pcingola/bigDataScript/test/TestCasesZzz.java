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
	public void test28() {
		runAndCheck("test/run_28.bds", "events", "[done]");
	}

	//	@Test
	//	public void test02() {
	//		verbose = true;
	//		runAndCheckpoint("test/checkpoint_02.bds", null, "l", "15");
	//	}
}
