package ca.mcgill.mcb.pcingola.bigDataScript.test;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	/**
	 * Make sure taskId contains 'taskName' parameter
	 * In this test 'taskName' is not safe to be used with as file name, so it has to be sanitized
	 */
	@Test
	public void test133_taskName_unsafe() {
		String out = runAndReturnStdout("test/run_133.bds");
		Assert.assertTrue(out.contains("run_133.mytask_unsafe_with_spaces"));
	}

}
