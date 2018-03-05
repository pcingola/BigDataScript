package org.bds.test;

import org.bds.util.Gpr;
import org.bds.util.Timer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test28() {
		Gpr.debug("Test");
		runAndCheck("test/run_28.bds", "events", "[done]");
	}

	@Test
	public void test29() {
		Gpr.debug("Test");
		runAndCheck("test/run_29.bds", "events", "[runnning, wait, done]");
	}

	@Test
	public void test30() {
		Gpr.debug("Test");
		runAndCheck("test/run_30.bds", "events", "[runnning, wait, done]");
	}

	@Test
	public void test31() {
		Gpr.debug("Test");
		Timer timer = new Timer();
		timer.start();
		runAndCheck(1, "test/run_31.bds", "events", "[runnning, kill, done]");
		Assert.assertTrue(timer.elapsed() < 1 * 1000); // We should finish in much less than 1 secs (the program waits 60secs)
	}

}
