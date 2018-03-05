package org.bds.test;

import org.bds.util.Gpr;
import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test45() {
		Gpr.debug("Test");
		String errs = "ERROR [ file 'test/test45.bds', line 2 ] :	Cannot append int to string[]";
		compileErrors("test/test45.bds", errs);
	}

}
