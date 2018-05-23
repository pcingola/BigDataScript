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
	public void test160_cluster_prelude() {
		Gpr.debug("Test");
		String args[] = { "-c", "test/test159_prelude_task.config" };
		runAndCheckStdout("test/run_159.bds", "=== TASK PRELUDE local ===", args, false);
	}

}
