package org.bds.test;

import java.io.File;

import org.bds.Config;
import org.bds.data.Data;
import org.bds.data.DataHttp;
import org.bds.data.DataRemote;
import org.bds.util.Gpr;
import org.junit.Assert;
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
	public void test01_parse_URLs_http() {
		Gpr.debug("Test");

		Data d = Data.factory("http://www.google.com/index.html");
		d.setVerbose(verbose);
		d.setDebug(debug);
		long lastMod = d.getLastModified().getTime();
		if (verbose) Gpr.debug("Path: " + d.getPath() + "\tlastModified: " + lastMod + "\tSize: " + d.size());

		// Check some features
		Assert.assertTrue(d instanceof DataHttp);
		Assert.assertEquals("http://www.google.com/index.html", d.getAbsolutePath());
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

}
