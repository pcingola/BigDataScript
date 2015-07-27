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

}
