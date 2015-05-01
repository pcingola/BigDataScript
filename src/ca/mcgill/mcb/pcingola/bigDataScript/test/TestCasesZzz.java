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
	public void test45() {
		String errs = "ERROR [ file 'test/test49.bds', line 4 ] :\tTask has empty statement";
		compileErrors("test/test49.bds", errs);
	}

}
