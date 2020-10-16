package org.bds.test;

import java.util.Random;

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

		String bucket = awsBucketName();
		String url = "s3://" + bucket + "/tmp/bds/remote_01/hello.txt";

		// Create S3 file
		Random rand = new Random();
		String txt = "OK: " + rand.nextLong();
		createS3File(url, txt);

		checkS3HelloTxt(url, bucket, "/tmp/bds/remote_01/hello.txt", "s3://" + bucket + "/tmp/bds/remote_01", txt);
	}

	@Test
	public void test17_S3() {
		Gpr.debug("Test");
		String bucket = awsBucketName();
		String expectedOutput = "" //
				+ "baseName       : test_remote_17.txt\n" //
				+ "baseName('txt'): test_remote_17\n" //
				+ "canRead        : true\n" //
				+ "canWrite       : true\n" //
				+ "dirName        : /tmp/bds/remote_17\n" //
				+ "extName        : txt\n" //
				+ "exists         : true\n" //
				+ "isDir          : false\n" //
				+ "isFile         : true\n" //
				+ "path           : /tmp/bds/remote_17/test_remote_17.txt\n" //
				+ "pathName       : /tmp/bds/remote_17\n" //
				+ "removeExt      : s3://" + bucket + "/tmp/bds/remote_17/test_remote_17\n" //
				+ "size           : 6\n" //
				+ "dirPath        : []\n" //
				+ "dir            : []\n" //
				+ "\n" //
				+ "Delete file s3://" + bucket + "/tmp/bds/remote_17/test_remote_17.txt\n" //
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

	// Task input local, output s3 file
	@Test
	public void test35() {
		runAndCheck("test/remote_35.bds", "outStr", "IN: 'remote_35'");
	}

	// Check task input in s3, output to s3
	@Test
	public void test36() {
		runAndCheck("test/remote_36.bds", "outStr", "IN: 'remote_36'");
	}

}
