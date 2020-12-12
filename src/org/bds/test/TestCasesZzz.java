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

	@Test
	public void test33_s3_dirPath() {
		Gpr.debug("Test");
		verbose = true;
		String bucket = awsBucketName();
		String region = awsRegion();

		String urlParen = "s3://" + bucket + "/tmp/bds/remote_33/test_remote_33";
		if (region != null && !region.isEmpty()) urlParen = "https://" + bucket + ".s3." + region + ".amazonaws.com/tmp/bds/remote_33/test_remote_33";

		// Create S3 files
		createS3File("s3://" + bucket + "/tmp/bds/remote_33/test_remote_33/bye.txt", region, "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_33/test_remote_33/bye_2.txt", region, "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_33/test_remote_33/hi.txt", region, "OK");
		createS3File("s3://" + bucket + "/tmp/bds/remote_33/test_remote_33/hi_2.txt", region, "OK");
		runAndCheck("test/remote_33.bds", "dd", "[" + urlParen + "/bye.txt, " + urlParen + "/bye_2.txt]");
	}

}
