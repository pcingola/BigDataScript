package org.bds.test.integration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.bds.Config;
import org.bds.data.Data;
import org.bds.data.DataHttp;
import org.bds.data.DataRemote;
import org.bds.run.BdsRun;
import org.bds.test.TestCasesBase;
import org.bds.util.Gpr;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for DataRemote files
 *
 * @author pcingola
 *
 */
public class TestCasesIntegrationDataRemote extends TestCasesBase {

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

	String getCurrPath() {
		try {
			return (new File(".")).getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Test parsing a remote HTTP file (DataHttp)
	 */
	@Test
	public void test01_parseUrlHttp() {
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

	/**
	 * Download a remote file using `file.download()`
	 */
	@Test
	public void test10_DownloadHttpFunction() {
		Gpr.debug("Test");
		runAndCheck("test/remote_10.bds", "locFile", "/tmp/bds/http/pcingola/github/io/BigDataScript/index.html");
	}

	/**
	 * Test Data.factory for remote HTTP file
	 */
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

	/**
	 * Download a remote file to a specific local file
	 */
	@Test
	public void test11_DownloadHttpToLocalFile() {
		Gpr.debug("Test");
		runAndCheck("test/remote_11.bds", "ok", "true");
	}

	/**
	 * Downloading with FTP
	 */
	@Test
	public void test19_FtpDownload() {
		Gpr.debug("Test");
		String localFilePath = runAndGet("test/remote_19.bds", "fLocal").toString();

		// Check that the file exists (remove tmp file after)
		File f = new File(localFilePath);
		Assert.assertTrue("Local file '" + localFilePath + "' does not exists", f.exists());
		f.delete();
	}

	/**
	 * Checking a remote FTP file exists
	 */
	@Test
	public void test20_FtpExists() {
		Gpr.debug("Test");
		runAndCheck("test/remote_20.bds", "fExists", "true");
	}

	/**
	 * Listing a remote FTP directory and getting the file base names
	 */
	@Test
	public void test21_FtpDirBasename() {
		Gpr.debug("Test");
		runAndCheck("test/remote_21.bds", "dHasReadme", "true");
	}

	/**
	 * Listing a remote FTP directory `d.dirPath()`
	 */
	@Test
	public void test22_FtpDirPath() {
		Gpr.debug("Test");
		runAndCheck("test/remote_22.bds", "dHasReadme", "true");
	}

	/**
	 * Downloading an FTP remote file using user credentials
	 */
	@Test
	public void test23_FtpDownloadWithUserAndPasswd() {
		Gpr.debug("Test");
		String localFilePath = runAndGet("test/remote_23.bds", "fLocal").toString();

		// Check that the file exists (remove tmp file after)
		File f = new File(localFilePath);
		Assert.assertTrue("Local file '" + localFilePath + "' does not exists", f.exists());
		f.delete();
	}

	/**
	 * Checking a remote FTP file exists, using user credentials
	 */
	@Test
	public void test24_FtpExistsWithUserAndPasswd() {
		Gpr.debug("Test");
		runAndCheck("test/remote_24.bds", "fExists", "true");
	}

	/**
	 * Listing a remote FTP directory, using user credentials: `d.dir()`
	 */
	@Test
	public void test25_FtpDirWithUserAndPasswd() {
		Gpr.debug("Test");
		runAndCheck("test/remote_25.bds", "dHasReadme", "true");
	}

	/**
	 * Listing a remote FTP directory, using user credentials: `d.dirPath()`)
	 */
	@Test
	public void test26_FtpDirPathWithUserAndPasswd() {
		Gpr.debug("Test");
		runAndCheck("test/remote_26.bds", "dHasReadme", "true");
	}

	/**
	 * Listing a remote FTP directory using regex: `d.dir(regex)`
	 */
	@Test
	public void test27_FtpDirRegex() {
		Gpr.debug("Test");
		runAndCheck("test/remote_27.bds", "dd", "[Homo_sapiens.GRCh37.75.dna.toplevel.fa.gz]");
	}

	/**
	 * Listing a remote FTP directory using regex (`dirPath(regex)`)
	 */
	@Test
	public void test28_FtpDirPathRegex() {
		Gpr.debug("Test");
		runAndCheck("test/remote_28.bds", "dd", "[ftp://ftp.ensembl.org/pub/release-75/fasta/homo_sapiens/dna/Homo_sapiens.GRCh37.75.dna.toplevel.fa.gz]");
	}

	/**
	 * Listing a remote HTTP directory using regex (`dirPath(regex)`)
	 */
	@Test
	public void test29_HttpDirPathRegex() {
		Gpr.debug("Test");
		runAndCheck("test/remote_29.bds", "dd", "[http://ftp.ensembl.org/pub/release-75/fasta/homo_sapiens/dna/Homo_sapiens.GRCh37.75.dna.toplevel.fa.gz]");
	}

	/**
	 * Listing a remote HTTP directory using regex (`dir(regex)`)
	 */
	@Test
	public void test30_HttpDirRegex() {
		Gpr.debug("Test");
		runAndCheck("test/remote_30.bds", "dd", "[Homo_sapiens.GRCh37.75.dna.toplevel.fa.gz]");
	}

}
