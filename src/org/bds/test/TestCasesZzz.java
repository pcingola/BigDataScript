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

	//	@Test
	//	public void test_z() {
	//		verbose = true;
	//		//		debug = true;
	//		runAndCheckException("test/z.bds", "ConcurrentModificationException");
	//	}

	@Test
	public void test244_concurrent_modification() {
		runAndCheckException("test/run_244.bds", "ConcurrentModificationException");
	}

	@Test
	public void test249_concurrent_modification_hash() {
		runAndCheckException("test/run_249.bds", "ConcurrentModificationException");
	}

}
