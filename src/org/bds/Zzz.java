package org.bds;

import java.io.File;

import org.bds.util.Gpr;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

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

		String bucket = awsBucketName();
		String key = "/tmp/zzz.txt";
		String localFileName = "z.txt";

		S3Client s3 = S3Client.create();
		s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), RequestBody.fromFile(new File(localFileName)));

		System.out.println("End");
	}

}