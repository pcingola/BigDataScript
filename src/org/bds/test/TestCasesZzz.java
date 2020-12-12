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

	// Task input in s3, output local file
	@Test
	public void test34() {
		verbose = true;
		runAndCheck("test/remote_34.bds", "outStr", "OK");
	}
	//
	//	// Task input local, output s3 file
	//	@Test
	//	public void test35() {
	//		runAndCheck("test/remote_35.bds", "outStr", "IN: 'remote_35'");
	//	}
	//
	//	// Check task input in s3, output to s3
	//	@Test
	//	public void test36() {
	//		runAndCheck("test/remote_36.bds", "outStr", "IN: 'remote_36'");
	//	}

}
