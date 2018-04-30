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
	public void test01_parse_URLs_s3() {
		Gpr.debug("Test");
		verbose = true;
		String url = "https://s3.amazonaws.com/pcingola.bds/hello.txt";
		checkS3HelloTxt(url, url, "https://s3.amazonaws.com/pcingola.bds");
	}

	//	@Test
	//	public void test01_parse_URLs_s3_02() {
	//		Gpr.debug("Test");
	//		String url = "s3://pcingola.bds/hello.txt";
	//		checkS3HelloTxt(url, url, "s3://pcingola.bds");
	//	}

}
