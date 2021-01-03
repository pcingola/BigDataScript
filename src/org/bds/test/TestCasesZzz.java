package org.bds.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

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
public class TestCasesZzz extends TestCasesBaseAws {

	@Before
	public void beforeEachTest() {
		BdsRun.reset();
		Config.get().load();
	}

	@Test
	public void test04_DependentTasksDepGoal() {
		Gpr.debug("Test");

		verbose = true;

		// Set the output file
		String name = "run_aws_04";
		String script = "test/" + name + ".bds";
		String inFile = "in.txt";
		String out1File = "out1.txt";
		String out2File = "out2.txt";

		// URLs in 's3://' style
		String urlInS3 = bucketUrlS3(name, inFile);
		String urlOut1S3 = bucketUrlS3(name, out1File);
		String urlOut2S3 = bucketUrlS3(name, out2File);

		// URLs (could be in 'https://' format if the region i sexplicitly defined)
		String urlIn = bucketUrl(name, inFile);
		String urlOut1 = bucketUrl(name, out1File);
		String urlOut2 = bucketUrl(name, out2File);

		Random rand = new Random();
		String inTxt = String.format("RAND_%x", rand.nextLong());
		log("Input text: " + inTxt);

		// Expected output:
		StringBuilder sb = new StringBuilder();
		// Main script (local)
		sb.append("Files:\n");
		sb.append("\tin: '" + urlInS3 + "'\n");
		sb.append("\tout1: '" + urlOut1S3 + "'\n");
		sb.append("\tout2: '" + urlOut2S3 + "'\n");
		sb.append("Before task1\n");
		sb.append("After task1\n");
		sb.append("Before task2\n");
		sb.append("After task2\n");
		sb.append("Goal: '" + urlOut2S3 + "'\n");
		// Task1 (aws, instance 1)
		sb.append("Start: Dep1 improper\n");
		sb.append("Input text: '" + inTxt + "'\n");
		sb.append("End: Dep1 Improper\n");
		// Task2 (aws, instance 2)
		sb.append("Start: Dep2\n");
		sb.append("Input:\n");
		sb.append("OUT1: '" + inTxt + "'\n");
		sb.append("End: Dep2\n");
		// Main script (local)
		sb.append("Done\n");
		String expectedStdout = sb.toString();

		// Cleanup: Make sure output file is deleted before starting
		String region = awsRegion();
		DataS3 din = new DataS3(urlIn, region);
		DataS3 dout1 = new DataS3(urlOut1, region);
		DataS3 dout2 = new DataS3(urlOut2, region);
		din.delete();
		dout1.delete();
		dout2.delete();

		// Run script and check that 'expectedStdout' is in STDOUT
		String args[] = { "-inTxt", inTxt };
		runAndCheckStdout(script, expectedStdout, null, args, false);

		// Check: Check that the output file exists
		assertTrue("Input file '" + din + "' does not exists", din.exists());
		assertTrue("Output file '" + dout1 + "' does not exists", dout1.exists());
		assertTrue("Output file '" + dout2 + "' does not exists", dout2.exists());

		// Check: Check file contents
		String dinExpected = inTxt;
		checkS3File(din, dinExpected);

		String dout1Expected = "OUT1: '" + inTxt + "'";
		checkS3File(dout1, dout1Expected);

		String dout2Expected = "OUT2\n" + dout1Expected;
		checkS3File(dout2, dout2Expected);

		// Check that there were two tasks executed by this script on AWS
		List<String> tids = getDoneTaskIdsAws();
		assertEquals("The number of tasks executed on AWS doesn't match the expected, tids (size=" + tids.size() + "): '" + tids + "'", 2, tids.size());

		// Cleanup
		din.delete();
		dout1.delete();
		dout2.delete();
	}

}
