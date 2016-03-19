package org.bds.test;

import org.bds.util.Gpr;
import org.bds.util.Timer;
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
	public void test27() {
		Gpr.debug("Test");
		verbose = true;
		Timer timer = new Timer();
		timer.start();
		runAndCheck(1, "test/run_27.bds", "timeout", "1"); // 2 seconds timeout
		Assert.assertTrue(timer.elapsed() < 3 * 1000); // We should finish in less than 3 secs (the program waits 60secs)
	}

}
