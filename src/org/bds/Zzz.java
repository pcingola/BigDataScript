package org.bds;

import org.bds.executioner.QueueThread;
import org.bds.executioner.QueueThreadAwsSqs;
import org.bds.util.Gpr;

public class Zzz {

	public static boolean debug = true;
	public static boolean verbose = true;

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

		Config config = new Config();
		config.setVerbose(true);

		QueueThread qt = new QueueThreadAwsSqs(config, null, null);
		qt.start();
		try {
			qt.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("End");
	}
}