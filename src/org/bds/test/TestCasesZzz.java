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
	public void test145_switch() {
		Gpr.debug("Test");
		runAndCheck("test/run_145.bds", "out", 3);
	}

	@Test
	public void test146_switch_fallthrough() {
		Gpr.debug("Test");
		runAndCheck("test/run_146.bds", "out", 35);
	}

	@Test
	public void test147_switch_default() {
		Gpr.debug("Test");
		runAndCheck("test/run_147.bds", "out", 100);
	}

	@Test
	public void test148_switch_default_fallthrough() {
		Gpr.debug("Test");
		runAndCheck("test/run_148.bds", "out", 700);
	}

	@Test
	public void test24_switch() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_24.bds", "test/checkpoint_24.chp", "out", 103);
	}

	@Test
	public void test25_switch() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_25.bds", "test/checkpoint_25.chp", "out", 35);
	}

	@Test
	public void test26_switch() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_26.bds", "test/checkpoint_26.chp", "out", 56);
	}

}
