package org.bds.test;

import org.bds.Config;
import org.junit.Before;

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
