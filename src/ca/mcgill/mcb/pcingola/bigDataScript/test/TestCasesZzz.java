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
	public void test115_task_dependency_using_taskId() {
		String stdout = runAndReturnStdout("test/run_115.bds");
		Assert.assertEquals("Hi 1\nBye 1\nHi 2\nBye 2\n", stdout);
	}

}
