package org.bds.test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.bds.Config;
import org.bds.data.Data;
import org.bds.data.DataFile;
import org.bds.data.DataHttp;
import org.bds.data.DataRemote;
import org.bds.data.DataS3;
import org.bds.util.Gpr;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesRemote extends TestCasesBase {

	@Before
	public void beforeEachTest() {
		Config.reset();
		Config.get().load();
	}

	String getCurrPath() {
		try {
			return (new File(".")).getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
		Assert.assertEquals(currPath + "/tmp.txt", d.getAbsolutePath());
		Gpr.toFile(d.getAbsolutePath(), "test");
		Assert.assertTrue(d.isFile());
		Assert.assertFalse(d.isDirectory());

		d = Data.factory("./tmp.txt");
		if (verbose) Gpr.debug("Path: " + d.getPath() + "\tabsolutePath: " + d.getAbsolutePath());
		Assert.assertTrue(d instanceof DataFile);
		Assert.assertEquals(currPath + "/./tmp.txt", d.getAbsolutePath());
		Assert.assertEquals(currPath + "/tmp.txt", d.getCanonicalPath());

		d = Data.factory("/tmp.txt");
		if (verbose) Gpr.debug("Path: " + d.getPath());
		Assert.assertTrue(d instanceof DataFile);
		Assert.assertEquals("/tmp.txt", d.getAbsolutePath());

		d = Data.factory("file:///tmp.txt");
		if (verbose) Gpr.debug("Path: " + d.getPath());
		Assert.assertTrue(d instanceof DataFile);
		Assert.assertEquals("/tmp.txt", d.getAbsolutePath());
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
		Assert.assertEquals("http://www.google.com/index.html", d.toString());
		Assert.assertEquals("http://www.google.com/", d.getParent().toString());
		Assert.assertEquals("/index.html", d.getPath());
		Assert.assertEquals("/index.html", d.getAbsolutePath());
		Assert.assertEquals("/index.html", d.getCanonicalPath());
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

		String bucket = awsBucketName();
		String url = "s3://" + bucket + "/tmp/bds/remote_01/hello.txt";

		// Create S3 file
		Random rand = new Random();
		String txt = "OK: " + rand.nextLong();
		createS3File(url, txt);

		checkS3HelloTxt(url, bucket, "/tmp/bds/remote_01/hello.txt", "s3://" + bucket + "/tmp/bds/remote_01", txt);
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
	public void test101_data_file_absolute() {
		String path = "/dir/sub/file.txt";
		Data dfile = Data.factory(path);
		Assert.assertFalse("Relative: " + dfile.isRelative(), dfile.isRelative());
		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);

		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), path, dfile.getAbsolutePath());
	}

	@Test
	public void test101b_data_file_absolute() {
		String path = "/dir/sub/file.txt";
		Data dfile = Data.factory("file://" + path);
		Assert.assertFalse("Relative: " + dfile.isRelative(), dfile.isRelative());
		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);

		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), path, dfile.getAbsolutePath());
	}

	@Test
	public void test102_data_file_relative() {
		String path = "dir/sub/file.txt";
		Data dfile = Data.factory(path);

		Assert.assertTrue("Relative: " + dfile.isRelative(), dfile.isRelative());
		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);

		Data pwd = Data.factory("");
		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), pwd.getAbsolutePath() + "/" + path, dfile.getAbsolutePath());
	}

	@Test
	public void test102b_data_file_relative() {
		String path = "dir/sub/file.txt";
		Data dfile = Data.factory("file://" + path);

		Assert.assertTrue("Relative: " + dfile.isRelative(), dfile.isRelative());
		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);

		Data pwd = Data.factory("");
		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), pwd.getAbsolutePath() + "/" + path, dfile.getAbsolutePath());
	}

	@Test
	public void test103_data_file_relative() {
		String path = "./dir/sub/file.txt";
		Data dfile = Data.factory(path);
		Assert.assertTrue("Relative: " + dfile.isRelative(), dfile.isRelative());
		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);

		Data pwd = Data.factory("");
		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), pwd.getAbsolutePath() + "/" + path, dfile.getAbsolutePath());
	}

	@Test
	public void test103b_data_file_relative() {
		String path = "./dir/sub/file.txt";
		Data dfile = Data.factory("file://" + path);
		Assert.assertTrue("Relative: " + dfile.isRelative(), dfile.isRelative());
		Assert.assertFalse("Exists: " + dfile.exists(), dfile.exists());
		Assert.assertTrue("Wrong data type: " + dfile.getClass().getCanonicalName(), dfile instanceof DataFile);

		Data pwd = Data.factory("");
		Assert.assertEquals("Path: " + dfile.getPath(), path, dfile.getPath());
		Assert.assertEquals("Canonical: " + dfile.getAbsolutePath(), pwd.getAbsolutePath() + "/" + path, dfile.getAbsolutePath());
	}

	@Test
	public void test104_file_join_segments() {
		Data dfile = Data.factory("dir/sub/file.txt");
		Data ddir = Data.factory("/home");
		Data djoin = ddir.join(dfile);

		Assert.assertTrue(dfile.isRelative());
		Assert.assertFalse(ddir.isRelative());
		Assert.assertFalse(djoin.isRelative());

		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(ddir instanceof DataFile);
		Assert.assertTrue(djoin instanceof DataFile);

		Assert.assertEquals("Path: " + djoin.getPath(), "/home/dir/sub/file.txt", djoin.getPath());
		Assert.assertEquals("Canonical: " + djoin.getAbsolutePath(), "/home/dir/sub/file.txt", djoin.getAbsolutePath());
	}

	@Test
	public void test104b_file_join_segments() {
		Data dfile = Data.factory("file://dir/sub/file.txt");
		Data ddir = Data.factory("file:///home");
		Data djoin = ddir.join(dfile);

		Assert.assertTrue(dfile.isRelative());
		Assert.assertFalse(ddir.isRelative());
		Assert.assertFalse(djoin.isRelative());

		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(ddir instanceof DataFile);
		Assert.assertTrue(djoin instanceof DataFile);

		Assert.assertEquals("Path: " + djoin.getPath(), "/home/dir/sub/file.txt", djoin.getPath());
		Assert.assertEquals("Canonical: " + djoin.getAbsolutePath(), "/home/dir/sub/file.txt", djoin.getAbsolutePath());
	}

	@Test
	public void test105_file_join_segments() {
		Data dfile = Data.factory("./dir/sub/file.txt");
		Data ddir = Data.factory("/home");
		Data djoin = ddir.join(dfile);

		Assert.assertTrue(dfile.isRelative());
		Assert.assertFalse(ddir.isRelative());
		Assert.assertFalse(djoin.isRelative());

		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(ddir instanceof DataFile);
		Assert.assertTrue(djoin instanceof DataFile);

		Assert.assertEquals("Path: " + djoin.getPath(), "/home/./dir/sub/file.txt", djoin.getPath());
		Assert.assertEquals("Canonical: " + djoin.getAbsolutePath(), "/home/./dir/sub/file.txt", djoin.getAbsolutePath());
	}

	@Test
	public void test105b_file_join_segments() {
		Data dfile = Data.factory("file://./dir/sub/file.txt");
		Data ddir = Data.factory("file:///home");
		Data djoin = ddir.join(dfile);

		Assert.assertTrue(dfile.isRelative());
		Assert.assertFalse(ddir.isRelative());
		Assert.assertFalse(djoin.isRelative());

		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(ddir instanceof DataFile);
		Assert.assertTrue(djoin instanceof DataFile);

		Assert.assertEquals("Path: " + djoin.getPath(), "/home/./dir/sub/file.txt", djoin.getPath());
		Assert.assertEquals("Canonical: " + djoin.getAbsolutePath(), "/home/./dir/sub/file.txt", djoin.getAbsolutePath());
	}

	@Test
	public void test106_url() {
		String url = "http://www.google.com";
		Data durl = Data.factory(url);
		Assert.assertFalse("Relative: " + durl.isRelative(), durl.isRelative());
		Assert.assertTrue("Exists: " + durl.exists(), durl.exists());
		Assert.assertFalse("Is dir: " + durl.isDirectory(), durl.isDirectory());
		Assert.assertTrue("Wrong data type: " + durl.getClass().getCanonicalName(), durl instanceof DataHttp);

		Assert.assertEquals("Path: " + durl.getPath(), "", durl.getPath());
		Assert.assertEquals("Canonical: " + durl.getAbsolutePath(), "", durl.getAbsolutePath());
		Assert.assertEquals("URL: " + durl.toString(), url, durl.toString());
	}

	@Test
	public void test107_url_join() {
		Data dfile = Data.factory("/dir/sub/file.txt");
		Data durl = Data.factory("http://www.ensembl.org");
		Data djoin = durl.join(dfile);

		Assert.assertFalse(dfile.isRelative());
		Assert.assertFalse(durl.isRelative());
		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(durl instanceof DataHttp);
		Assert.assertTrue(djoin instanceof DataHttp);
		DataHttp dhttp = (DataHttp) djoin;
		Assert.assertEquals(djoin.getPath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
		Assert.assertEquals(dhttp.toString(), "http://www.ensembl.org/dir/sub/file.txt");
	}

	@Test
	public void test107b_url_join() {
		Data dfile = Data.factory("dir/sub/file.txt");
		Data durl = Data.factory("http://www.ensembl.org");
		Data djoin = durl.join(dfile);

		Assert.assertTrue(dfile.isRelative());
		Assert.assertFalse(durl.isRelative());
		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(durl instanceof DataHttp);
		Assert.assertTrue(djoin instanceof DataHttp);
		DataHttp dhttp = (DataHttp) djoin;
		Assert.assertEquals(djoin.getPath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
		Assert.assertEquals(dhttp.toString(), "http://www.ensembl.org/dir/sub/file.txt");
	}

	@Test
	public void test108_s3_join() {
		Data dfile = Data.factory("/dir/sub/file.txt");
		Data ds3 = Data.factory("s3://my_bucket");

		Assert.assertFalse(dfile.isRelative());
		Assert.assertFalse(ds3.isRelative());
		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(ds3 instanceof DataS3);
		Data djoin = ds3.join(dfile);

		Assert.assertTrue(djoin instanceof DataS3);
		DataS3 ds3join = (DataS3) djoin;
		Assert.assertEquals(djoin.getPath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
		Assert.assertEquals(ds3join.toString(), "s3://my_bucket/dir/sub/file.txt");
	}

	@Test
	public void test108b_s3_join() {
		Data dfile = Data.factory("dir/sub/file.txt");
		Data ds3 = Data.factory("s3://my_bucket");
		Data djoin = ds3.join(dfile);

		Assert.assertTrue(dfile.isRelative());
		Assert.assertFalse(ds3.isRelative());
		Assert.assertTrue(dfile instanceof DataFile);
		Assert.assertTrue(ds3 instanceof DataS3);
		Assert.assertTrue(djoin instanceof DataS3);
		Assert.assertEquals(djoin.getPath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getAbsolutePath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.getCanonicalPath(), "/dir/sub/file.txt");
		Assert.assertEquals(djoin.toString(), "s3://my_bucket/dir/sub/file.txt");
	}

	@Test
	public void test109_parseFile_relative() throws Exception {
		String path = "tmp.txt";
		Data dpath = Data.factory(path);
		if (verbose) Gpr.debug("Path: " + dpath.getPath());
		Assert.assertTrue(dpath instanceof DataFile);
		Assert.assertTrue(dpath.isRelative());

		// Absolute path
		File fpathAbs = new File(new File("").getAbsoluteFile(), path);
		Assert.assertEquals(fpathAbs.getAbsolutePath(), dpath.getAbsolutePath());

		// Canonical path
		File fpathCan = new File(new File(".").getCanonicalFile(), path);
		Assert.assertEquals(fpathCan.getCanonicalPath(), dpath.getCanonicalPath());

		Gpr.toFile(dpath.getAbsolutePath(), "test");
		Assert.assertTrue(dpath.isFile());
		Assert.assertFalse(dpath.isDirectory());
	}

	@Test
	public void test11_download() {
		Gpr.debug("Test");
		runAndCheck("test/remote_11.bds", "ok", "true");
	}

	@Test
	public void test110_parseFile_relative_03() throws Exception {
		String path = "./tmp.txt";
		Data dpath = Data.factory(path);
		if (verbose) Gpr.debug("Path: " + dpath.getPath());
		Assert.assertTrue(dpath instanceof DataFile);
		Assert.assertTrue(dpath.isRelative());

		// Absolute path
		File fpathAbs = new File(new File("").getAbsoluteFile(), path);
		Assert.assertEquals(fpathAbs.getAbsolutePath(), dpath.getAbsolutePath());

		// Canonical path
		File fpathCan = new File(new File(".").getCanonicalFile(), path);
		Assert.assertEquals(fpathCan.getCanonicalPath(), dpath.getCanonicalPath());

		Gpr.toFile(dpath.getAbsolutePath(), "test");
		Assert.assertTrue(dpath.isFile());
		Assert.assertFalse(dpath.isDirectory());
	}

	@Test
	public void test12_upload() {
		Gpr.debug("Test");
		runAndCheck("test/remote_12.bds", "ok", "true");
	}

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

	@Test
	public void test14_S3() {
		Gpr.debug("Test");
		String bucket = awsBucketName();

		// Create S3 file (create local file and upload)
		String s3file = "s3://" + bucket + "/tmp/bds/remote_14/hello.txt";
		createS3File(s3file, "OK");

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

	@Test
	public void test15_S3() {
		Gpr.debug("Test");
		String bucket = awsBucketName();

		// Create S3 files
		createS3File("s3://" + bucket + "/tmp/bds/remote_15/test_dir/z1.txt", "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_15/test_dir/z2.txt", "OK");

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
				+ "dirPath        : [s3://" + bucket + "/tmp/bds/remote_15/test_dir/z1.txt, s3://" + bucket + "/tmp/bds/remote_15/test_dir/z2.txt]\n" //
				+ "dir            : [z1.txt, z2.txt]\n" //
		;

		runAndCheckStdout("test/remote_15.bds", expectedOutput);
	}

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

	@Test
	public void test18_S3() {
		Gpr.debug("Test");
		runAndCheck("test/remote_18.bds", "ok", "true");
	}

	@Test
	public void test19_ftp_download() {
		Gpr.debug("Test");
		String localFilePath = runAndGet("test/remote_19.bds", "fLocal").toString();

		// Check that the file exists (remove tmp file after)
		File f = new File(localFilePath);
		Assert.assertTrue("Local file '" + localFilePath + "' does not exists", f.exists());
		f.delete();
	}

	@Test
	public void test20_ftp_exists() {
		Gpr.debug("Test");
		runAndCheck("test/remote_20.bds", "fExists", "true");
	}

	@Test
	public void test21_ftp_dir() {
		Gpr.debug("Test");
		runAndCheck("test/remote_21.bds", "dHasReadme", "true");
	}

	@Test
	public void test22_ftp_dir() {
		Gpr.debug("Test");
		runAndCheck("test/remote_22.bds", "dHasReadme", "true");
	}

	@Test
	public void test23_ftp_download_with_user() {
		Gpr.debug("Test");
		String localFilePath = runAndGet("test/remote_23.bds", "fLocal").toString();

		// Check that the file exists (remove tmp file after)
		File f = new File(localFilePath);
		Assert.assertTrue("Local file '" + localFilePath + "' does not exists", f.exists());
		f.delete();
	}

	@Test
	public void test24_ftp_exists() {
		Gpr.debug("Test");
		runAndCheck("test/remote_24.bds", "fExists", "true");
	}

	@Test
	public void test25_ftp_dir() {
		Gpr.debug("Test");
		runAndCheck("test/remote_25.bds", "dHasReadme", "true");
	}

	@Test
	public void test26_ftp_dir() {
		Gpr.debug("Test");
		runAndCheck("test/remote_26.bds", "dHasReadme", "true");
	}

	@Test
	public void test27_ftp_dir() {
		Gpr.debug("Test");
		runAndCheck("test/remote_27.bds", "dd", "[Homo_sapiens.GRCh37.75.dna.toplevel.fa.gz]");
	}

	@Test
	public void test28_ftp_dirPath() {
		Gpr.debug("Test");
		runAndCheck("test/remote_28.bds", "dd", "[ftp://ftp.ensembl.org/pub/release-75/fasta/homo_sapiens/dna/Homo_sapiens.GRCh37.75.dna.toplevel.fa.gz]");
	}

	@Test
	public void test29_http_dir() {
		Gpr.debug("Test");
		runAndCheck("test/remote_29.bds", "dd", "[http://ftp.ensembl.org/pub/release-75/fasta/homo_sapiens/dna/Homo_sapiens.GRCh37.75.dna.toplevel.fa.gz]");
	}

	@Test
	public void test31_s3_dir() {
		Gpr.debug("Test");

		String bucket = awsBucketName();

		// Create S3 files
		createS3File("s3://" + bucket + "/tmp/bds/remote_31/test_remote_31/bye.txt", "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_31/test_remote_31/bye_2.txt", "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_31/test_remote_31/hi.txt", "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_31/test_remote_31/hi_2.txt", "OK");

		runAndCheck("test/remote_31.bds", "dd", "[bye.txt, bye_2.txt, hi.txt, hi_2.txt]");
	}

	@Test
	public void test32_s3_dirPath() {
		Gpr.debug("Test");
		String bucket = awsBucketName();

		// Create S3 files
		createS3File("s3://" + bucket + "/tmp/bds/remote_32/test_remote_32/bye.txt", "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_32/test_remote_32/bye_2.txt", "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_32/test_remote_32/hi.txt", "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_32/test_remote_32/hi_2.txt", "OK");

		runAndCheck("test/remote_32.bds", "dd", "[bye.txt, bye_2.txt]");
	}

	@Test
	public void test33_s3_dirPath() {
		Gpr.debug("Test");
		String bucket = awsBucketName();

		// Create S3 files
		createS3File("s3://" + bucket + "/tmp/bds/remote_33/test_remote_33/bye.txt", "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_33/test_remote_33/bye_2.txt", "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_33/test_remote_33/hi.txt", "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_33/test_remote_33/hi_2.txt", "OK");
		runAndCheck("test/remote_33.bds", "dd", "[s3://" + bucket + "/tmp/bds/remote_33/test_remote_33/bye.txt, s3://" + bucket + "/tmp/bds/remote_33/test_remote_33/bye_2.txt]");
	}

	// Task input in s3, output local file
	@Test
	public void test34() {
		runAndCheck("test/remote_34.bds", "outStr", "OK");
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
