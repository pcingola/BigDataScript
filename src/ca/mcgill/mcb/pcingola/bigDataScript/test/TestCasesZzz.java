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

	@Test
	public void test15_goal_using_list() {
		String out = runAndReturnStdout("test/graph_15.bds");
		Assert.assertTrue(out.contains("Hi 0\n"));
		Assert.assertTrue(out.contains("Hi 1\n"));
		Assert.assertTrue(out.contains("Hi 2\n"));
	}

}
