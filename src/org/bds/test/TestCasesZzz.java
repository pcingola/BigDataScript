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

	// TODO: Task output in s3
	@Test
	public void test34() {
		// TODO: Get bucket name from $BDS_HOME/aws_test_bucket.txt
		runAndCheck("test/remote_34.bds", null);
	}

	// TODO: Check task input in s3, output to s3
	@Test
	public void test36() {
	}

	// TODO: Check task multiple outputs to s3
	@Test
	public void test37() {
	}

	// TODO: Check task multiple inputs and multiple outputs in s3
	@Test
	public void test38() {
	}

	// TODO: Check task local inputs, out1 -> out2 -> out3 in S3 (cached input for the next task)
	@Test
	public void test39() {
	}

	// TODO: Check task local inputs, out -> out -> out in S3 (detect an error or at least a WARNING?)
	@Test
	public void test40() {
	}

}
