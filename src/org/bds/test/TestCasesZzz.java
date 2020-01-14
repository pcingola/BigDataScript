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
	public void test244_concurrent_modification() {
		verbose = true;
		runOk("test/run_244.bds");
	}

	@Test
	public void test245_out_of_bounds() {
		verbose = true;
		runAndCheck("test/run_245.bds", "ret", "5");
	}

	@Test
	public void test246_out_of_bounds() {
		verbose = true;
		runAndCheck("test/run_246.bds", "ret", "1");
	}

	@Test
	public void test247_out_of_bounds() {
		verbose = true;
		runAndCheck("test/run_247.bds", "ret", "5");
	}

	@Test
	public void test248_out_of_bounds() {
		verbose = true;
		runAndCheck("test/run_248.bds", "ret", "1");
	}

}
