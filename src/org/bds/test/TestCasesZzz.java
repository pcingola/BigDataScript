package org.bds.test;

import org.bds.Config;
import org.bds.data.DataS3;
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

	//	/**
	//	 * Execute several tasks within a 'par' function
	//	 */
	//	@Test
	//	public void test06() {
	//		Gpr.debug("Test");
	//		verbose = true;
	//		List<String> expected = new ArrayList<>();
	//		expected.add("Task improper: Before, a=42");
	//		for (int i = 0; i < 5; i++) {
	//			expected.add("Task improper: Start, a=42, i=" + i);
	//			expected.add("Task improper: End, a=" + (42 + i) + ", i=" + i);
	//		}
	//		expected.add("Task improper: After, a=42");
	//
	//		String stdout = runAndCheckStdout("test/run_task_improper_06.bds", expected, null, false);
	//		Assert.assertTrue("Should finish with a 'Done' message", stdout.endsWith("Done\n"));
	//	}
	//

	// Save and load remote checkpoint (S3)
	@Test
	public void test30() {
		Gpr.debug("Test");
		verbose = debug = true;
		String bucket = awsBucketName();
		String region = awsRegion();
		String checkpointFile = "https://" + bucket + ".s3." + region + "." + DataS3.AWS_S3_VIRTUAL_HOSTED_DOMAIN + "/tmp/bds/checkpoint_30.chp";
		runAndCheckpoint("test/checkpoint_30.bds", checkpointFile, "sum", 285, null);
	}

	//	// Regional S3 addressing
	//	// Reference: https://aws.amazon.com/blogs/aws/amazon-s3-path-deprecation-plan-the-rest-of-the-story/
	//	@Test
	//	public void test01() {
	//		String bucket = awsBucketName();
	//		String awsRegion = awsRegion();
	//		String url = "https://" + bucket + ".s3." + awsRegion + ".amazonaws.com/tmp/bds/checkpoint_30.chp";
	//		Data d = Data.factory(url);
	//		System.err.println(d);
	//	}

}
