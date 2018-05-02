package org.bds.test;

import org.bds.Config;
import org.bds.util.Gpr;
import org.junit.Before;
import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Before
	public void beforeEachTest() {
		Config.reset();
		Config.get().load();
	}

	@Test
	public void test156_list_int_sort() {
		Gpr.debug("Test");
		verbose = true;
		runAndCheck("test/run_156.bds", "sl", "[-99, -1, 1, 2, 3, 9, 23, 99, 101]");
	}

}
