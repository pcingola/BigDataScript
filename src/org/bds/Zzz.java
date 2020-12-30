package org.bds;

import org.bds.util.Gpr;

public class Zzz {

	public static boolean debug = true;
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

		Config config = new Config();
		config.setVerbose(true);

		String programFileName = "../../task_id_1234.programFileName";
		//		Task task = new Task("task_id_1234", programFileName, "echo hi\n", "z.bds", 10);
		//		task.createProgramFile();
		//
		//		QueueThread qt = new QueueThreadAwsSqs(config, null, null, QUEUE_NAME);
		//		qt.start();
		//		try {
		//			qt.add(task);
		//			qt.wait();
		//		} catch (InterruptedException e) {
		//			e.printStackTrace();
		//		}

		System.out.println("End");
	}
}