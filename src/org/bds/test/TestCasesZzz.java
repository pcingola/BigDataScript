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

	//	@Test
	//	public void test31_s3_dir() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		runAndCheck("test/remote_31.bds", "dd", "[bye.txt, bye_2.txt, hi.txt, hi_2.txt]");
	//	}

	@Test
	public void test32_s3_dir_regex() {
		Gpr.debug("Test");
		verbose = true;
		runAndCheck("test/remote_32.bds", "dd", "[bye.txt, bye_2.txt, hi.txt, hi_2.txt]");
	}

	//	@Test
	//	public void test15_S3() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		String expectedOutput = "" //
	//				+ "baseName       : \n" //
	//				+ "baseName('txt'): \n" //
	//				+ "canRead        : true\n" //
	//				+ "canWrite       : true\n" //
	//				+ "dirName        : s3://pcingola.bds/test_dir\n" //
	//				+ "extName        : bds/test_dir/\n" //
	//				+ "exists         : true\n" //
	//				+ "isDir          : true\n" //
	//				+ "isFile         : false\n" //
	//				+ "path           : s3://pcingola.bds/test_dir/\n" //
	//				+ "pathName       : s3://pcingola.bds/test_dir\n" //
	//				+ "removeExt      : s3://pcingola\n" //
	//				+ "dirPath        : [s3://pcingola.bds/test_dir/z1.txt, s3://pcingola.bds/test_dir/z2.txt]\n" //
	//				+ "dir            : [z1.txt, z2.txt]\n" //
	//		;
	//
	//		runAndCheckStdout("test/remote_15.bds", expectedOutput);
	//	}

}
