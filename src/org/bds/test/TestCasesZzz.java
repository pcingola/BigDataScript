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
	public void test16_S3() {
		Gpr.debug("Test");
		verbose = true;
		String expectedOutput = "" //
				+ "baseName       : test_remote_16.txt\n" //
				+ "baseName('txt'): test_remote_16\n" //
				+ "canRead        : true\n" //
				+ "canWrite       : true\n" //
				+ "dirName        : \n" //
				+ "extName        : txt\n" //
				+ "exists         : true\n" //
				+ "isDir          : false\n" //
				+ "isFile         : true\n" //
				+ "path           : /test_remote_16.txt\n" //
				+ "pathName       : \n" //
				+ "removeExt      : s3://pcingola.bds/test_remote_16\n" //
				+ "size           : 6\n" //
				+ "dirPath        : []\n" //
				+ "dir            : []\n" //
		;

		runAndCheckStdout("test/remote_16.bds", expectedOutput);
	}

	@Test
	public void test17_S3() {
		Gpr.debug("Test");
		String expectedOutput = "" //
				+ "baseName       : test_remote_17.txt\n" //
				+ "baseName('txt'): test_remote_17\n" //
				+ "canRead        : true\n" //
				+ "canWrite       : true\n" //
				+ "dirName        : \n" //
				+ "extName        : txt\n" //
				+ "exists         : true\n" //
				+ "isDir          : false\n" //
				+ "isFile         : true\n" //
				+ "path           : /test_remote_17.txt\n" //
				+ "pathName       : \n" //
				+ "removeExt      : s3://pcingola.bds/test_remote_17\n" //
				+ "size           : 6\n" //
				+ "dirPath        : []\n" //
				+ "dir            : []\n" //
				+ "\n" //
				+ "Delete file s3://pcingola.bds/test_remote_17.txt\n" //
				+ "canRead        : false\n" //
				+ "canWrite       : false\n" //
				+ "exists         : false\n" //
				+ "size           : 0\n" //
				+ "dirPath        : []\n" //
				+ "dir            : []\n" //
		;

		runAndCheckStdout("test/remote_17.bds", expectedOutput);
	}

	@Test
	public void test18_S3() {
		Gpr.debug("Test");
		runAndCheck("test/remote_18.bds", "ok", "true");
	}

	@Test
	public void test244_concurrent_modification() {
		verbose = true;
		runOk("test/run_244.bds");
	}

}
