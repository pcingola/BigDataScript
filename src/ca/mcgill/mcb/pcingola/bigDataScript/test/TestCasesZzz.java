package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test26() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("l0", 1);
		expectedValues.put("l1", 2);
		expectedValues.put("l2", 3);
		expectedValues.put("l3", 4);
		expectedValues.put("l4", 5);
		expectedValues.put("s", 5);
		runAndCheckMultiple("test/run_26.bds", expectedValues);
	}

	@Test
	public void test27() {
		Timer timer = new Timer();
		timer.start();
		runAndCheck("test/run_27.bds", "timeout", "1"); // 2 seconds timeout
		Assert.assertTrue(timer.elapsed() < 3 * 1000); // We should finish in less than 3 secs (the program waits 60secs)
	}

	@Test
	public void test28() {
		runAndCheck("test/run_28.bds", "events", "[done]");
	}

	@Test
	public void test29() {
		runAndCheck("test/run_29.bds", "events", "[runnning, wait, done]");
	}

	@Test
	public void test30() {
		runAndCheck("test/run_30.bds", "events", "[runnning, wait, done]");
	}

}
