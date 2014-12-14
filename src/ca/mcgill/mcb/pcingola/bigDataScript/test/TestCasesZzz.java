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
	public void test116_lineWrap_backslashId() {
		String stdout = runAndReturnStdout("test/run_116.bds");
		Assert.assertEquals("hi bye\nThe answer\t\tis: 42", stdout);
	}

}
