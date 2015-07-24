package ca.mcgill.mcb.pcingola.bigDataScript.data;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * A bucket / object in AWS S3
 *
 * @author pcingola
 */
public class DataS3 extends DataRemote {

	private static int BUFFER_SIZE = 100 * 1024;

	public static final String DEFAULT_AWS_REGION = Regions.US_EAST_1.toString();
	public static final String AWS_DOMAIN = "amazonaws.com";
	public static final String AWS_S3_PREFIX = "s3";

	private static Map<Region, AmazonS3> s3ByRegion = new HashMap<>();

	Region region;
	String bucketName;
	String key;

	public DataS3(URL url) {
		super(url);
		parse(url.toString());
		canWrite = false;
	}

	@Override
	public boolean delete() {
		if (!isFile()) return false; // Do not delete bucket
		getS3().deleteObject(bucketName, key);
		return true;
	}

	@Override
	public void deleteOnExit() {
		if (verbose) Timer.showStdErr("Cannot delete file '" + this + "'");
	}

	/**
	 * Download a file
	 */
	@Override
	public boolean download(String localFile) {
		try {
			if (!isFile()) return false;

			S3Object s3object = getS3().getObject(new GetObjectRequest(bucketName, key));
			if (verbose) System.out.println("Downloading '" + this + "'");
			updateInfo(s3object);

			// Create local file
			mkDirsLocalPath();
			FileOutputStream os = new FileOutputStream(getLocalPath());

			// Copy S3 object to file
			S3ObjectInputStream is = s3object.getObjectContent();
			int count = 0, total = 0, lastShown = 0;
			byte data[] = new byte[BUFFER_SIZE];
			while ((count = is.read(data, 0, BUFFER_SIZE)) != -1) {
				os.write(data, 0, count);
				total += count;

				if (verbose) {
					// Show every MB
					if ((total - lastShown) > (1024 * 1024)) {
						System.err.print(".");
						lastShown = total;
					}
				}
			}
			if (verbose) System.err.println("");

			// Close streams
			is.close();
			os.close();
			if (verbose) Timer.showStdErr("Donwload finished. Total " + total + " bytes.");

			// Update last modified info
			updateLocalFileLastModified();

			return true;
		} catch (Exception e) {
			Timer.showStdErr("ERROR while downloading " + this);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create an S3 client.
	 * S3 clients are thread safe, thus it is encouraged to have
	 * only one client instead of instantiating one each time.
	 */
	protected AmazonS3 getS3() {
		AmazonS3 s3 = s3ByRegion.get(region);

		if (s3 == null) {
			// Create singleton in a thread safe way
			synchronized (s3ByRegion) {
				if (s3ByRegion.get(region) == null) {
					// Create client
					s3 = new AmazonS3Client();
					s3.setRegion(region);
					s3ByRegion.put(region, s3);
				}
			}
		}

		return s3;
	}

	/**
	 * Is this a bucket?
	 */
	@Override
	public boolean isDirectory() {
		return key == null;
	}

	@Override
	public boolean isFile() {
		return key != null;
	}

	@Override
	public ArrayList<String> list() {
		ArrayList<String> list = new ArrayList<>();

		ObjectListing objectListing = getS3().listObjects(new ListObjectsRequest().withBucketName(bucketName));
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			list.add(objectSummary.toString());
			// list.add(objectSummary.getKey());
		}

		return list;
	}

	/**
	 * There is no concept of directory in S3
	 * Paths created automatically
	 */
	@Override
	public boolean mkdirs() {
		return true;
	}

	/**
	 * Parse string representing an AWS S3 URI
	 */
	protected void parse(String s3UriStr) {
		AmazonS3URI s3uri = new AmazonS3URI(s3UriStr);

		bucketName = s3uri.getBucket();
		key = s3uri.getKey();

		// Parse and set region
		String regionStr = s3uri.getRegion();
		try {
			if (regionStr == null) regionStr = Config.get().getString(Config.AWS_REGION, DEFAULT_AWS_REGION);
			region = Region.getRegion(Regions.valueOf(regionStr.toUpperCase()));
			Gpr.debug("REGION: " + region);
		} catch (Exception e) {
			throw new RuntimeException("Cannot parse AWS region '" + regionStr + "'", e);
		}
	}

	/**
	 * Connect and update info
	 */
	@Override
	protected boolean updateInfo() {
		// Read metadata
		S3Object s3object = getS3().getObject(new GetObjectRequest(bucketName, key));
		return updateInfo(s3object);
	}

	/**
	 * Update object's information
	 */
	protected boolean updateInfo(S3Object s3object) {
		// Read metadata
		ObjectMetadata om = s3object.getObjectMetadata();

		// Update data
		size = om.getContentLength();
		canRead = true;
		lastModified = om.getLastModified();
		exists = true;
		latestUpdate = new Timer(CACHE_TIMEOUT).start();

		// Show information
		if (verbose) Timer.showStdErr("Updated infromation for '" + getUrl() + "'"//
				+ "\n\tcanRead      : " + canRead //
				+ "\n\texists       : " + exists //
				+ "\n\tlast modified: " + lastModified //
				+ "\n\tsize         : " + size //
				);

		return true;
	}

	/**
	 * Cannot upload to a web server
	 */
	@Override
	public boolean upload(String localFileName) {
		// Create and check file
		File file = new File(getLocalPath());
		if (!file.exists() || !file.isFile() || !file.canRead()) {
			if (debug) Gpr.debug("Error accessing local file '" + getLocalPath() + "'");
			return false;
		}

		// Upload
		getS3().putObject(new PutObjectRequest(bucketName, key, file));
		return true;
	}
}
