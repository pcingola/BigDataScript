package org.bds.test;

import org.bds.Config;
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
	public void test158_log() {
		verbose = true;
		runAndCheckStderr("test/run_158.bds", "hi there");
	}

}
