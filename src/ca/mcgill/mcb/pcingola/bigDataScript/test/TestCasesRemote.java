package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.data.Data;
import ca.mcgill.mcb.pcingola.bigDataScript.data.DataFile;
import ca.mcgill.mcb.pcingola.bigDataScript.data.DataHttp;
import ca.mcgill.mcb.pcingola.bigDataScript.data.DataRemote;
import ca.mcgill.mcb.pcingola.bigDataScript.data.DataS3;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import junit.framework.Assert;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesRemote extends TestCasesBase {
	/**
	 * Check a 'hello.txt' file in an S3 bucket
	 */
	void checkS3HelloTxt(String url, String canPath, String paren) {
		int objectSize = 12;
		long lastModified = 1437862027000L;

		Data d = Data.factory(url);
		d.setVerbose(verbose);
		d.setDebug(debug);
		long lastMod = d.getLastModified().getTime();
		if (verbose) Gpr.debug("Path: " + d.getPath() + "\tlastModified: " + lastMod + "\tSize: " + d.size());

		// Check some features
		Assert.assertTrue("Is S3?", d instanceof DataS3);
		Assert.assertEquals(url, d.getCanonicalPath());
		Assert.assertEquals(objectSize, d.size());
		Assert.assertEquals(lastModified, d.getLastModified().getTime());
		Assert.assertTrue("Is file?", d.isFile());
		Assert.assertFalse("Is directory?", d.isDirectory());

		// Download file
		boolean ok = d.download();
		Assert.assertTrue("Download OK", ok);
		Assert.assertTrue("Is downloaded?", d.isDownloaded());

		// Is it at the correct local file?
		Assert.assertEquals("/tmp/bds/s3/pcingola.bds/hello.txt", d.getLocalPath());
		Assert.assertEquals(canPath, d.getCanonicalPath());
		Assert.assertEquals(paren, d.getParent());
		Assert.assertEquals("hello.txt", d.getName());

		// Check last modified time
		File file = new File(d.getLocalPath());
		long lastModLoc = file.lastModified();
		Assert.assertTrue("Last modified check:" //
				+ "\n\tlastMod    : " + lastMod //
				+ "\n\tlastModLoc : " + lastModLoc //
				+ "\n\tDiff       : " + (lastMod - lastModLoc)//
				, Math.abs(lastMod - lastModLoc) < 2 * DataRemote.CACHE_TIMEOUT);

		Assert.assertEquals(objectSize, file.length());
	}

	@Test
	public void test01_parse_URLs_file() {
		Gpr.debug("Test");

		String currPath;
		try {
			currPath = (new File(".")).getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (verbose) Gpr.debug("CurrPath: " + currPath);

		Data d = Data.factory("tmp.txt");
		if (verbose) Gpr.debug("Path: " + d.getPath());
		Assert.assertTrue(d instanceof DataFile);
		Assert.assertEquals(currPath + "/tmp.txt", d.getCanonicalPath());
		Gpr.toFile(d.getCanonicalPath(), "test");
		Assert.assertTrue(d.isFile());
		Assert.assertFalse(d.isDirectory());

		d = Data.factory("./tmp.txt");
		if (verbose) Gpr.debug("Path: " + d.getPath());
		Assert.assertTrue(d instanceof DataFile);
		Assert.assertEquals(currPath + "/tmp.txt", d.getCanonicalPath());

		d = Data.factory("/tmp.txt");
		if (verbose) Gpr.debug("Path: " + d.getPath());
		Assert.assertTrue(d instanceof DataFile);
		Assert.assertEquals("/tmp.txt", d.getCanonicalPath());

		d = Data.factory("file:///tmp.txt");
		if (verbose) Gpr.debug("Path: " + d.getPath());
		Assert.assertTrue(d instanceof DataFile);
		Assert.assertEquals("/tmp.txt", d.getCanonicalPath());
	}

	@Test
	public void test01_parse_URLs_http() {
		Gpr.debug("Test");

		Data d = Data.factory("http://www.google.com/index.html");
		d.setVerbose(verbose);
		d.setDebug(debug);
		long lastMod = d.getLastModified().getTime();
		if (verbose) Gpr.debug("Path: " + d.getPath() + "\tlastModified: " + lastMod + "\tSize: " + d.size());

		// Check some features
		Assert.assertTrue(d instanceof DataHttp);
		Assert.assertEquals("http://www.google.com/index.html", d.getCanonicalPath());
		Assert.assertEquals("http://www.google.com/", d.getParent());
		Assert.assertEquals("/index.html", d.getPath());
		Assert.assertEquals("index.html", d.getName());
		Assert.assertTrue("Is file?", d.isFile());
		Assert.assertFalse("Is directory?", d.isDirectory());
		Assert.assertFalse("Is downloaded?", d.isDownloaded());

		// Download file
		boolean ok = d.download();
		Assert.assertTrue("Download OK", ok);

		// Is it at the correct local file?
		Assert.assertEquals("/tmp/bds/http/www/google/com/index.html", d.getLocalPath());

		// Check last modified time
		File file = new File(d.getLocalPath());
		long lastModLoc = file.lastModified();
		Assert.assertTrue("Last modified check:" //
				+ "\n\tlastMod    : " + lastMod //
				+ "\n\tlastModLoc : " + lastModLoc //
				+ "\n\tDiff       : " + (lastMod - lastModLoc)//
				, Math.abs(lastMod - lastModLoc) < 2 * DataRemote.CACHE_TIMEOUT);
	}

	@Test
	public void test01_parse_URLs_s3() {
		Gpr.debug("Test");
		String url = "http://pcingola.bds.s3.amazonaws.com/hello.txt";
		checkS3HelloTxt(url, url, "http://pcingola.bds.s3.amazonaws.com");
	}

	@Test
	public void test01_parse_URLs_s3_02() {
		Gpr.debug("Test");
		String url = "s3://pcingola.bds/hello.txt";
		checkS3HelloTxt(url, url, "s3://pcingola.bds");
	}

	@Test
	public void test03_task_URL() {
		Gpr.debug("Test");
		runAndCheck("test/remote_03.bds", "first", "<!DOCTYPE html>");
	}

	@Test
	public void test04_task_URL() {
		Gpr.debug("Test");
		runAndCheck("test/remote_04.bds", "first", "<!DOCTYPE html>");
	}

	@Test
	public void test05_task_URL() {
		Gpr.debug("Test");
		runAndCheck("test/remote_05.bds", "first", "<!DOCTYPE html>");
	}

	@Test
	public void test06_task_URL() {
		Gpr.debug("Test");
		runAndCheck("test/remote_06.bds", "first", "<!DOCTYPE html>");
	}

	@Test
	public void test07_task_URL() {
		Gpr.debug("Test");
		runAndCheck("test/remote_07.bds", "first", "<!DOCTYPE html>");
	}

	@Test
	public void test08_task_URL() {
		Gpr.debug("Test");
		runAndCheck("test/remote_08.bds", "first", "<!DOCTYPE html>");
	}

	@Test
	public void test09_task_URL() {
		Gpr.debug("Test");
		runAndCheck("test/remote_09.bds", "first", "<!DOCTYPE html>");
	}

	@Test
	public void test10_download() {
		Gpr.debug("Test");
		runAndCheck("test/remote_10.bds", "locFile", "/tmp/bds/http/pcingola/github/io/BigDataScript/index.html");
	}

	@Test
	public void test11_download() {
		Gpr.debug("Test");
		runAndCheck("test/remote_11.bds", "ok", "true");
	}

	@Test
	public void test12_upload() {
		Gpr.debug("Test");
		runAndCheck("test/remote_12.bds", "ok", "true");
	}

	@Test
	public void test13_S3() {
		Gpr.debug("Test");
		String expectedOutput = "" //
				+ "canonical      : test_remote_13_file_does_not_exits_in_S3.txt\n" //
				+ "baseName       : test_remote_13_file_does_not_exits_in_S3.txt\n" //
				+ "baseName('txt'): test_remote_13_file_does_not_exits_in_S3\n" //
				+ "canRead        : false\n" + "canWrite       : false\n" //
				+ "dirName        : s3://pcingola.bds\n" //
				+ "extName        : txt\n" //
				+ "exists         : false\n" + "isDir          : false\n" //
				+ "isFile         : true\n" //
				+ "path           : s3://pcingola.bds/test_remote_13_file_does_not_exits_in_S3.txt\n" //
				+ "pathName       : s3://pcingola.bds\n" //
				+ "removeExt      : s3://pcingola.bds/test_remote_13_file_does_not_exits_in_S3\n" //
				+ "dirPath        : []\n" //
				+ "dir            : []\n" //
				;

		runAndCheckStdout("test/remote_13.bds", expectedOutput);
	}

	@Test
	public void test14_S3() {
		Gpr.debug("Test");
		String expectedOutput = "" //
				+ "baseName       : hello.txt\n" //
				+ "baseName('txt'): hello\n" //
				+ "canRead        : true\n" //
				+ "canWrite       : true\n" //
				+ "dirName        : s3://pcingola.bds\n" //
				+ "extName        : txt\n" //
				+ "exists         : true\n" //
				+ "isDir          : false\n" //
				+ "isFile         : true\n" //
				+ "path           : s3://pcingola.bds/hello.txt\n" //
				+ "pathName       : s3://pcingola.bds\n" //
				+ "removeExt      : s3://pcingola.bds/hello\n" //
				+ "dirPath        : []\n" //
				+ "dir            : []\n" //
				;

		runAndCheckStdout("test/remote_14.bds", expectedOutput);
	}

	@Test
	public void test15_S3() {
		Gpr.debug("Test");
		String expectedOutput = "" //
				+ "baseName       : \n" //
				+ "baseName('txt'): \n" //
				+ "canRead        : true\n" //
				+ "canWrite       : true\n" //
				+ "dirName        : s3://pcingola.bds/test_dir\n" //
				+ "extName        : bds/test_dir/\n" //
				+ "exists         : true\n" //
				+ "isDir          : true\n" //
				+ "isFile         : false\n" //
				+ "path           : s3://pcingola.bds/test_dir/\n" //
				+ "pathName       : s3://pcingola.bds/test_dir\n" //
				+ "removeExt      : s3://pcingola\n" //
				+ "dirPath        : [s3://pcingola.bds/test_dir/z1.txt, s3://pcingola.bds/test_dir/z2.txt]\n" //
				+ "dir            : [z1.txt, z2.txt]\n" //
				;

		runAndCheckStdout("test/remote_15.bds", expectedOutput);
	}

	@Test
	public void test16_S3() {
		Gpr.debug("Test");
		String expectedOutput = "" //
				+ "baseName       : test_remote_16.txt\n" //
				+ "baseName('txt'): test_remote_16\n" //
				+ "canRead        : true\n" //
				+ "canWrite       : true\n" //
				+ "dirName        : s3://pcingola.bds\n" //
				+ "extName        : txt\n" //
				+ "exists         : true\n" //
				+ "isDir          : false\n" //
				+ "isFile         : true\n" //
				+ "path           : s3://pcingola.bds/test_remote_16.txt\n" //
				+ "pathName       : s3://pcingola.bds\n" //
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
				+ "dirName        : s3://pcingola.bds\n" //
				+ "extName        : txt\n" //
				+ "exists         : true\n" //
				+ "isDir          : false\n" //
				+ "isFile         : true\n" //
				+ "path           : s3://pcingola.bds/test_remote_17.txt\n" //
				+ "pathName       : s3://pcingola.bds\n" //
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

}
