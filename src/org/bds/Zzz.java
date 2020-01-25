package org.bds;

import java.io.File;

import org.bds.data.DataFile;
import org.bds.util.Gpr;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class Zzz {

	public static boolean debug = true;
	public static boolean verbose = true;

	public static void main(String[] args) {
		System.out.println("Start");

		String localFileName = "/tmp/bds/s3/pcingola.bds/hello.txt";
		DataFile local = new DataFile(localFileName);
		Gpr.debug("LOCAL ABS PATH: " + local.getAbsolutePath());
		Gpr.debug("LOCAL SIZE: " + local.size());

		String bucket = "pcingola.bds";
		String key = "zzz.txt";

		AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
		s3.putObject(new PutObjectRequest(bucket, key, new File(localFileName)));

		//		DataS3 ds3 = new DataS3("s3://" + bucket + "/" + key);
		//		ds3.upload(local);

		System.out.println("End");
	}

}