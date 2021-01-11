package org.bds.test.integration;

import org.bds.data.DataS3;
import org.bds.test.TestCasesBaseAws;
import org.bds.util.Gpr;
import org.junit.Test;

public class TestCasesIntegrationCheckpointAws extends TestCasesBaseAws {

	public static boolean debug = false;

	/**
	 * Test creating a remote checkpoint on AWS S3
	 */
	@Test
	public void test29() {
		Gpr.debug("Test");
		runAndCheck("test/checkpoint_29.bds", "ok", true);
	}

	/**
	 * Test creating a checkpoint on AWS S3 and recovering from it
	 */
	@Test
	public void test30() {
		Gpr.debug("Test");
		String bucket = awsBucketName();
		String region = awsRegion();
		String checkpointFile = "https://" + bucket + ".s3." + region + "." + DataS3.AWS_S3_VIRTUAL_HOSTED_DOMAIN + "/tmp/bds/checkpoint_30.chp";
		runAndCheckpoint("test/checkpoint_30.bds", checkpointFile, "sum", 285, null);
	}

}
