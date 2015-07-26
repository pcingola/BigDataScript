package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.data.Data;
import ca.mcgill.mcb.pcingola.bigDataScript.data.DataRemote;
import ca.mcgill.mcb.pcingola.bigDataScript.data.DataS3;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	/**
	 * Check a 'hello.txt' file in an S3 bucket
	 */
	void checkS3HelloTxt(String url) {
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
		Assert.assertEquals("s3://pcingola.bds/hello.txt", d.getCanonicalPath());
		Assert.assertEquals("s3://pcingola.bds", d.getParent());
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

	//	@Test
	//	public void test01_parse_URLs_file() {
	//		Gpr.debug("Test");
	//
	//		String currPath;
	//		try {
	//			currPath = (new File(".")).getCanonicalPath();
	//		} catch (IOException e) {
	//			throw new RuntimeException(e);
	//		}
	//		if (verbose) Gpr.debug("CurrPath: " + currPath);
	//
	//		Data d = Data.factory("tmp.txt");
	//		if (verbose) Gpr.debug("Path: " + d.getPath());
	//		Assert.assertTrue(d instanceof DataFile);
	//		Assert.assertEquals(currPath + "/tmp.txt", d.getCanonicalPath());
	//		Gpr.toFile(d.getCanonicalPath(), "test");
	//		Assert.assertTrue(d.isFile());
	//		Assert.assertFalse(d.isDirectory());
	//
	//		d = Data.factory("./tmp.txt");
	//		if (verbose) Gpr.debug("Path: " + d.getPath());
	//		Assert.assertTrue(d instanceof DataFile);
	//		Assert.assertEquals(currPath + "/tmp.txt", d.getCanonicalPath());
	//
	//		d = Data.factory("/tmp.txt");
	//		if (verbose) Gpr.debug("Path: " + d.getPath());
	//		Assert.assertTrue(d instanceof DataFile);
	//		Assert.assertEquals("/tmp.txt", d.getCanonicalPath());
	//
	//		d = Data.factory("file:///tmp.txt");
	//		if (verbose) Gpr.debug("Path: " + d.getPath());
	//		Assert.assertTrue(d instanceof DataFile);
	//		Assert.assertEquals("/tmp.txt", d.getCanonicalPath());
	//	}
	//
	//	@Test
	//	public void test01_parse_URLs_http() {
	//		Gpr.debug("Test");
	//
	//		Data d = Data.factory("http://www.google.com/index.html");
	//		d.setVerbose(verbose);
	//		d.setDebug(debug);
	//		long lastMod = d.getLastModified().getTime();
	//		if (verbose) Gpr.debug("Path: " + d.getPath() + "\tlastModified: " + lastMod + "\tSize: " + d.size());
	//
	//		// Check some features
	//		Assert.assertTrue(d instanceof DataHttp);
	//		Assert.assertEquals("http://www.google.com/index.html", d.getCanonicalPath());
	//		Assert.assertEquals("http://www.google.com/", d.getParent());
	//		Assert.assertEquals("/index.html", d.getPath());
	//		Assert.assertEquals("index.html", d.getName());
	//		Assert.assertTrue("Is file?", d.isFile());
	//		Assert.assertFalse("Is directory?", d.isDirectory());
	//		Assert.assertFalse("Is downloaded?", d.isDownloaded());
	//
	//		// Download file
	//		boolean ok = d.download();
	//		Assert.assertTrue("Download OK", ok);
	//
	//		// Is it at the correct local file?
	//		Assert.assertEquals("/tmp/bds/http/www/google/com/index.html", d.getLocalPath());
	//
	//		// Check last modified time
	//		File file = new File(d.getLocalPath());
	//		long lastModLoc = file.lastModified();
	//		Assert.assertTrue("Last modified check:" //
	//				+ "\n\tlastMod    : " + lastMod //
	//				+ "\n\tlastModLoc : " + lastModLoc //
	//				+ "\n\tDiff       : " + (lastMod - lastModLoc)//
	//		, Math.abs(lastMod - lastModLoc) < 2 * DataRemote.CACHE_TIMEOUT);
	//	}
	//
	//	@Test
	//	public void test01_parse_URLs_s3() {
	//		Gpr.debug("Test");
	//		String url = "http://pcingola.bds.s3.amazonaws.com/hello.txt";
	//		checkS3HelloTxt(url);
	//	}
	//
	//	@Test
	//	public void test01_parse_URLs_s3_02() {
	//		Gpr.debug("Test");
	//		String url = "s3://pcingola.bds/hello.txt";
	//		checkS3HelloTxt(url);
	//	}
	//
	//	@Test
	//	public void test03_task_URL() {
	//		verbose = true;
	//		runAndCheck("test/remote_03.bds", "first", "<!DOCTYPE html>");
	//	}
	//
	@Test
	public void test04_task_URL() {
		verbose = true;
		runAndCheck("test/remote_04.bds", "first", "<!DOCTYPE html>");
	}

	//	// Program 4:
	//	task cat "$in" > $out
	//
	//	// Program 5:
	//	task cat '$in' > $out
	//
	//	// Program 6:
	//	task cat "$in\"this is not changed" > $out
	//
	//	// Program 7:
	//	task ( out <- [in, in2] ) {
	//		sys cat $in > $out
	//	}
	//
	//	// Program 8:
	//  inList := [$in, $in2]
	//	task ( out <- inList ) {
	//		sys cat $in > $out
	//	}
	//
	//	// Program 9:
	//	task ( 'file.txt' <- 'http://www.google.com/index.html' ) {
	//		sys cat http://www.google.com/index.html > file.txt
	//	}
	//
	//	}
	//
	//	@Test
	//	public void test04_task_URL_download() {
	//	}
	//
	//	@Test
	//	public void test05_task_URL_upload() {
	//	}

}
