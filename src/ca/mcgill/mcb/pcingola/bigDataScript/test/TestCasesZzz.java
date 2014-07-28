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
	public void test14() {
		runAndCheck("test/run_14.bds", "s", "this is string interpolation: int i = 42 and str = \"hi\" and both hi42");
	}

}
