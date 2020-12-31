package org.bds.test;

import static org.junit.Assert.assertTrue;

import org.bds.Config;
import org.bds.data.DataS3;
import org.bds.run.BdsRun;
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
		BdsRun.reset();
		Config.get().load();
	}

	protected String bucketUrl(String dirName, String fileName) {
		// Set the output file
		String bucket = awsBucketName();
		String region = awsRegion();
		String urlParen = "s3://" + bucket + "/tmp/bds/" + dirName;
		if (!region.isEmpty()) urlParen = "https://" + bucket + ".s3." + region + ".amazonaws.com/tmp/bds/" + dirName;
		String url = urlParen + "/" + fileName;
		return url;
	}

	@Test
	public void test02_ImproperTask() {
		Gpr.debug("Test");

		String name = "run_aws_02";
		verbose = true;
		debug = true;

		// Set the output file
		String region = awsRegion();
		String url = bucketUrl(name, "out.txt");

		// Cleanup: Make sure output file is deleted before starting
		DataS3 dout = new DataS3(url, region);
		dout.delete();

		// Run script
		runOk("test/" + name + ".bds");

		// Check: Check that the output file exists
		assertTrue("Output file '" + dout + "' does not exists", dout.exists());

		// Cleanup
		dout.delete();
	}

}
