package org.bds.test.integration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Random;

import org.bds.Config;
import org.bds.data.DataRemote;
import org.bds.run.BdsRun;
import org.bds.test.TestCasesBaseAws;
import org.bds.util.Gpr;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for DataRemote files
 *
 * @author pcingola
 *
 */
public class TestCasesIntegrationDataRemoteS3 extends TestCasesBaseAws {

	@Before
	public void beforeEachTest() {
		BdsRun.reset();
		//		Config.reset();
		Config.get().load();

		// Delete 'tmp' download dir
		String tmpDownloadDir = Config.get().getTmpDir() + "/" + DataRemote.TMP_BDS_DATA;
		try {
			Files.walk(Paths.get(tmpDownloadDir)) //
					.map(Path::toFile) //
					.sorted(Comparator.reverseOrder()) //
					.forEach(File::delete);
		} catch (NoSuchFileException e) {
			// OK
		} catch (IOException e) {
			throw new RuntimeException("Error deleting tmp directory '" + tmpDownloadDir + "'", e);
		}
	}

	/**
	 * Test parsing a remote AWS S3 file (DataS3)
	 */
	@Test
	public void test01_parseUrlS3() {
		Gpr.debug("Test");

		String bucket = awsBucketName();
		String region = awsRegion();

		String urlParen = "s3://" + bucket + "/tmp/bds/remote_01";
		if (!region.isEmpty()) urlParen = "https://" + bucket + ".s3." + region + ".amazonaws.com/tmp/bds/remote_01";

		String url = urlParen + "/hello.txt";

		// Create S3 file
		Random rand = new Random();
		String txt = "OK: " + rand.nextLong();
		createS3File(url, region, txt);

		checkS3File(url, region, bucket, "/tmp/bds/remote_01/hello.txt", urlParen, txt);
	}

	/**
	 * Test parsing a remote AWS S3 file (DataS3) using "virtual hosting" style URL
	 */
	@Test
	public void test01_parseUrlS3VirtualHost() {
		Gpr.debug("Test");

		String bucket = awsBucketName();
		String region = awsRegion();
		String urlParen = "https://" + bucket + ".s3." + region + ".amazonaws.com/tmp/bds/remote_01";
		String url = urlParen + "/hello.txt";

		// Create S3 file
		Random rand = new Random();
		String txt = "OK: " + rand.nextLong();
		createS3File(url, region, txt);

		checkS3File(url, null, bucket, "/tmp/bds/remote_01/hello.txt", urlParen, txt);
	}

	/**
	 * Upload a local file to AWS S3
	 */
	@Test
	public void test12_UploadS3() {
		Gpr.debug("Test");
		runAndCheck("test/remote_12.bds", "ok", "true");
	}

	/**
	 * Parsing an AWS S3 file that does not exists with multiple bds functions
	 */
	@Test
	public void test13_S3() {
		Gpr.debug("Test");
		String bucket = awsBucketName();
		String expectedOutput = "" //
				+ "canonical      : test_remote_13_file_does_not_exits_in_S3.txt\n" //
				+ "baseName       : test_remote_13_file_does_not_exits_in_S3.txt\n" //
				+ "baseName('txt'): test_remote_13_file_does_not_exits_in_S3\n" //
				+ "canRead        : false\n" + "canWrite       : false\n" //
				+ "dirName        : /tmp/bds/remote_13\n" //
				+ "extName        : txt\n" //
				+ "exists         : false\n" + "isDir          : false\n" //
				+ "isFile         : true\n" //
				+ "path           : /tmp/bds/remote_13/test_remote_13_file_does_not_exits_in_S3.txt\n" //
				+ "pathName       : /tmp/bds/remote_13\n" //
				+ "removeExt      : s3://" + bucket + "/tmp/bds/remote_13/test_remote_13_file_does_not_exits_in_S3\n" //
				+ "dirPath        : []\n" //
				+ "dir            : []\n" //
		;

		runAndCheckStdout("test/remote_13.bds", expectedOutput);
	}

	/**
	 * Creating an S3 file and parsing with multiple bds functions
	 */
	@Test
	public void test14_S3() {
		Gpr.debug("Test");
		String bucket = awsBucketName();

		// Create S3 file (create local file and upload)
		String s3file = "s3://" + bucket + "/tmp/bds/remote_14/hello.txt";
		createS3File(s3file, awsRegion(), "OK");

		// Expected output
		String expectedOutput = "" //
				+ "baseName       : hello.txt\n" //
				+ "baseName('txt'): hello\n" //
				+ "canRead        : true\n" //
				+ "canWrite       : true\n" //
				+ "dirName        : /tmp/bds/remote_14\n" //
				+ "extName        : txt\n" //
				+ "exists         : true\n" //
				+ "isDir          : false\n" //
				+ "isFile         : true\n" //
				+ "path           : /tmp/bds/remote_14/hello.txt\n" //
				+ "pathName       : /tmp/bds/remote_14\n" //
				+ "removeExt      : s3://" + bucket + "/tmp/bds/remote_14/hello\n" //
				+ "dirPath        : []\n" //
				+ "dir            : []\n" //
		;

		runAndCheckStdout("test/remote_14.bds", expectedOutput);
	}

	/**
	 * Creating two S3 files and parsing with multiple bds functions
	 * Listing the "directory"
	 */
	@Test
	public void test15_S3() {
		Gpr.debug("Test");
		String bucket = awsBucketName();
		String region = awsRegion();

		// Create S3 files
		createS3File("s3://" + bucket + "/tmp/bds/remote_15/test_dir/z1.txt", region, "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_15/test_dir/z2.txt", region, "OK");

		String urlParen = "s3://" + bucket + "/tmp/bds/remote_15/test_dir";
		if (region != null && !region.isEmpty()) urlParen = "https://" + bucket + ".s3." + region + ".amazonaws.com/tmp/bds/remote_15/test_dir";

		String expectedOutput = "" //
				+ "baseName       : \n" //
				+ "baseName('txt'): \n" //
				+ "canRead        : true\n" //
				+ "canWrite       : true\n" //
				+ "dirName        : /tmp/bds/remote_15/test_dir\n" //
				+ "extName        : \n" //
				+ "exists         : true\n" //
				+ "isDir          : true\n" //
				+ "isFile         : false\n" //
				+ "path           : /tmp/bds/remote_15/test_dir/\n" //
				+ "pathName       : /tmp/bds/remote_15/test_dir\n" //
				+ "removeExt      : \n" //
				+ "dirPath        : [" + urlParen + "/z1.txt, " + urlParen + "/z2.txt]\n" //
				+ "dir            : [z1.txt, z2.txt]\n" //
		;

		runAndCheckStdout("test/remote_15.bds", expectedOutput);
	}

	/**
	 * Creating an S3 file using `write` function and parsing with multiple bds functions
	 */
	@Test
	public void test16_S3() {
		Gpr.debug("Test");
		String bucket = awsBucketName();

		String expectedOutput = "" //
				+ "baseName       : test_remote_16.txt\n" //
				+ "baseName('txt'): test_remote_16\n" //
				+ "canRead        : true\n" //
				+ "canWrite       : true\n" //
				+ "dirName        : /tmp/bds/remote_16\n" //
				+ "extName        : txt\n" //
				+ "exists         : true\n" //
				+ "isDir          : false\n" //
				+ "isFile         : true\n" //
				+ "path           : /tmp/bds/remote_16/test_remote_16.txt\n" //
				+ "pathName       : /tmp/bds/remote_16\n" //
				+ "removeExt      : s3://" + bucket + "/tmp/bds/remote_16/test_remote_16\n" //
				+ "size           : 6\n" //
				+ "dirPath        : []\n" //
				+ "dir            : []\n" //
		;

		runAndCheckStdout("test/remote_16.bds", expectedOutput);
	}

	/**
	 * Creating an S3 file using `file.write()` function and parsing with multiple bds functions
	 * Then deleting the file with `file.rm()` and re-parsing the same file to show it doesn't exists
	 */
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
				+ "size           : -1\n" //
				+ "dirPath        : []\n" //
				+ "dir            : []\n" //
		;

		runAndCheckStdout("test/remote_17.bds", expectedOutput);
	}

	/**
	 * Creating and reading an AWS S3 file
	 */
	@Test
	public void test18_S3WriteS3Read() {
		Gpr.debug("Test");
		runAndCheck("test/remote_18.bds", "ok", "true");
	}

	/**
	 * Creating and listing a remote AWS S3 directory using `path.dir()`
	 */
	@Test
	public void test31_S3Dir() {
		Gpr.debug("Test");

		String bucket = awsBucketName();

		// Create S3 files
		createS3File("s3://" + bucket + "/tmp/bds/remote_31/test_remote_31/bye.txt", awsRegion(), "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_31/test_remote_31/bye_2.txt", awsRegion(), "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_31/test_remote_31/hi.txt", awsRegion(), "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_31/test_remote_31/hi_2.txt", awsRegion(), "OK");

		runAndCheck("test/remote_31.bds", "dd", "[bye.txt, bye_2.txt, hi.txt, hi_2.txt]");
	}

	/**
	 * Creating and listing a remote AWS S3 directory using `path.dir(regex)`
	 */
	@Test
	public void test32_S3DirRegex() {
		Gpr.debug("Test");
		String bucket = awsBucketName();

		// Create S3 files
		createS3File("s3://" + bucket + "/tmp/bds/remote_32/test_remote_32/bye.txt", awsRegion(), "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_32/test_remote_32/bye_2.txt", awsRegion(), "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_32/test_remote_32/hi.txt", awsRegion(), "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_32/test_remote_32/hi_2.txt", awsRegion(), "OK");

		runAndCheck("test/remote_32.bds", "dd", "[bye.txt, bye_2.txt]");
	}

	/**
	 * Creating and listing a remote AWS S3 directory using `path.dirPath(regex)`
	 */
	@Test
	public void test33_S3DirPathRegex() {
		Gpr.debug("Test");
		String bucket = awsBucketName();
		String region = awsRegion();

		String urlParen = "s3://" + bucket + "/tmp/bds/remote_33/test_remote_33";
		if (region != null && !region.isEmpty()) urlParen = "https://" + bucket + ".s3." + region + ".amazonaws.com/tmp/bds/remote_33/test_remote_33";

		// Create S3 files
		createS3File("s3://" + bucket + "/tmp/bds/remote_33/test_remote_33/bye.txt", region, "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_33/test_remote_33/bye_2.txt", region, "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_33/test_remote_33/hi.txt", region, "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_33/test_remote_33/hi_2.txt", region, "OK");
		runAndCheck("test/remote_33.bds", "dd", "[" + urlParen + "/bye.txt, " + urlParen + "/bye_2.txt]");
	}

}
