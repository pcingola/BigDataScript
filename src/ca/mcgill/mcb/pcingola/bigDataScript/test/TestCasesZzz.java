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
	public void test44() {
		String errs = "ERROR [ file 'test/test44.bds', line 2 ] :	Cannot append int[] to string[]";
		compileErrors("test/test44.bds", errs);
	}

	@Test
	public void test45() {
		String errs = "ERROR [ file 'test/test45.bds', line 2 ] :	Cannot append int to string[]";
		compileErrors("test/test45.bds", errs);
	}

}
