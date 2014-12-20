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
	public void test14_serialize_method_call_args() {
		runAndCheckpoint("test/z.bds", "test/z.chp", "ok", "true");
	}
}
