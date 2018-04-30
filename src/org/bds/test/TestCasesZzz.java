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
	public void test51() {
		Gpr.debug("Test");
		verbose = true;
		String errs = "Cannot assign to non-variable";
		compileErrors("test/test51.bds", errs);
	}

}
