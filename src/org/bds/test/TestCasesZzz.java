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
	public void test01_parse_URLs_s3() {
		Gpr.debug("Test");
		String url = "http://pcingola.bds.s3.amazonaws.com/hello.txt";
		verbose = true;
		checkS3HelloTxt(url, url, "http://pcingola.bds.s3.amazonaws.com");
	}

	@Test
	public void test01_parse_URLs_s3_02() {
		Gpr.debug("Test");
		String url = "s3://pcingola.bds/hello.txt";
		checkS3HelloTxt(url, url, "s3://pcingola.bds");
	}

}
