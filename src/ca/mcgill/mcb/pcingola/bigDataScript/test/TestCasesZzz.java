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
	public void test32() {
		System.out.println("PATH=" + System.getenv("PATH"));
		runAndCheck("test/run_32.bds", "out", "Hi\n");
	}

}
