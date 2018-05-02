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
	public void test155_list_sort() {
		Gpr.debug("Test");
		verbose = true;
		runAndCheck("test/run_155.bds", "sl", "[a, a, a+a, a_a, aa, aaa, aaaa, aaaaaa, x, y, z]");
	}

}
