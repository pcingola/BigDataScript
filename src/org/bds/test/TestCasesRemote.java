package org.bds.test;

import java.io.File;
import java.io.IOException;

import org.bds.Config;
import org.bds.data.Data;
import org.bds.data.DataFile;
import org.bds.data.DataHttp;
import org.bds.data.DataRemote;
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
		Assert.assertEquals("http://www.google.com/index.html", d.getUri().toString());
		Assert.assertEquals("http://www.google.com/", d.getParent());
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
		runAndCheck("test/remote_27.bds", "dd", "[ftp://ftp.ensembl.org/pub/release-75/fasta/homo_sapiens/dna/Homo_sapiens.GRCh37.75.dna.toplevel.fa.gz]");
	}

	@Test
	public void test28_ftp_dirPath() {
		Gpr.debug("Test");
		runAndCheck("test/remote_28.bds", "dd", "[ftp://ftp.ensembl.org/pub/release-75/fasta/homo_sapiens/dna/Homo_sapiens.GRCh37.75.dna.toplevel.fa.gz]");
	}

	@Test
	public void test29_http_dir() {
		Gpr.debug("Test");
		runAndCheck("test/remote_29.bds", "dd", "[http://ftp.ensemblorg.ebi.ac.uk/pub/release-75/fasta/homo_sapiens/dna/Homo_sapiens.GRCh37.75.dna.toplevel.fa.gz]");
	}

	@Test
	public void test30_http_dir() {
		Gpr.debug("Test");
		runAndCheck("test/remote_30.bds", "dd", "[http://ftp.ensemblorg.ebi.ac.uk/pub/release-75/fasta/homo_sapiens/dna/Homo_sapiens.GRCh37.75.dna.toplevel.fa.gz]");
	}

}
