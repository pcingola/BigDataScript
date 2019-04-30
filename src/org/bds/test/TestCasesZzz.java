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

	//	@Test
	//	public void test19_ftp_download() {
	//		Gpr.debug("Test");
	//		String localFilePath = runAndGet("test/remote_19.bds", "fLocal").toString();
	//
	//		// Check that the file exists (remove tmp file after)
	//		File f = new File(localFilePath);
	//		Assert.assertTrue("Local file '" + localFilePath + "' does not exists", f.exists());
	//		f.delete();
	//	}
	//
	//	@Test
	//	public void test20_ftp_exists() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		runAndCheck("test/remote_20.bds", "fExist", "true");
	//	}
	//
	//
	//
	//	@Test
	//	public void test21_ftp_dir() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		runAndCheck("test/remote_21.bds", "dHasReadme", "true");
	//	}

	@Test
	public void test22_ftp_dir() {
		Gpr.debug("Test");
		verbose = true;
		runAndCheck("test/remote_22.bds", "dHasReadme", "true");
	}

	//	@Test
	//	public void test23_ftp_download_with_user() {
	//		Gpr.debug("Test");
	//		String localFilePath = runAndGet("test/remote_23.bds", "fLocal").toString();
	//
	//		// Check that the file exists (remove tmp file after)
	//		File f = new File(localFilePath);
	//		Assert.assertTrue("Local file '" + localFilePath + "' does not exists", f.exists());
	//		f.delete();
	//	}

}
