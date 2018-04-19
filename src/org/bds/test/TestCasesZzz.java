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
	public void test118_dependency_using_path() {
		Gpr.debug("Test");
		runAndCheckExit("test/run_118.bds", 0);
	}

}
