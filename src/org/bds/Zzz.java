package org.bds;

import org.bds.executioner.QueueThreadAwsSqs;
import org.bds.osCmd.CmdAws;
import org.bds.test.BdsTest;
import org.bds.util.Gpr;

public class Zzz {

	public static boolean debug = true;
	public static boolean log = true;
	public static boolean verbose = true;

	public static final String QUEUE_NAME = "bds_test_123456789";

	// Read bucket name from $HOME/.bds/aws_test_bucket.txt
	public static String awsBucketName() {
		String awsBucketNameFile = Gpr.HOME + "/.bds/aws_test_bucket.txt";
		String bucket = Gpr.readFile(awsBucketNameFile);
		bucket = bucket.split("\n")[0].trim();
		if (verbose) System.out.println("Bucket name: " + bucket);
		return bucket;
	}

	public static void main(String[] args) {
		System.out.println("Start");

		// Do not run instance
		CmdAws.DO_NOT_RUN_INSTANCE = true;
		QueueThreadAwsSqs.USE_QUEUE_NAME_DEBUG = true;

		String fileName = "test/z.bds";

		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.setLog(log);
		bdsTest.run();

		System.out.println("End");
	}
}