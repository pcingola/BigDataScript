package org.bds.test;

import org.bds.util.Gpr;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test116_lineWrap_backslashId() {
		Gpr.debug("Test");
		String stdout = runAndReturnStdout("test/run_116.bds");
		Assert.assertEquals("hi bye\nThe answer\t\tis: 42", stdout);
	}

}
