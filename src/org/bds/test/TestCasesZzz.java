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

	// Save and load remote checkpoint (S3)
	@Test
	public void test30() {
		Gpr.debug("Test");
		String bucket = awsBucketName();
		String checkpointFile = "s3://" + bucket + "/tmp/bds/checkpoint_30.chp";
		runAndCheckpoint("test/checkpoint_30.bds", checkpointFile, "sum", 285);
	}

}
