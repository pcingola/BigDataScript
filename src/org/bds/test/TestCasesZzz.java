package org.bds.test;

import org.bds.Config;
import org.junit.Before;

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

	//	// TODO: Check task multiple outputs to s3
	//	@Test
	//	public void test37() {
	//	}
	//
	//	// TODO: Check task multiple inputs and multiple outputs in s3
	//	@Test
	//	public void test38() {
	//	}
	//

}
