package org.bds.data;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bds.Config;
import org.bds.util.Gpr;
import org.bds.util.Timer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
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

	public static final String AWS_CONFIG_FILE = Gpr.HOME + "/.aws/config";
	public static final String AWS_CONFIG_REGION = "region";
	public static final String AWS_DOMAIN = "amazonaws.com";
	public static final String AWS_S3_PREFIX = "s3";
	public static final String AWS_S3_PROTOCOL = "s3://";
	public static final String DEFAULT_AWS_REGION = Regions.US_EAST_1.toString();

	private static Map<Region, AmazonS3> s3ByRegion = new HashMap<>();

	AmazonS3URI s3uri;
	Region region;
	String bucketName;
	String key;

	public DataS3(String urlStr) {
		super();
		parse(urlStr);
		canWrite = false;
	}

	/**
	 * Find the default region to use
	 */
	Region defaultRegion() {
		// Is there a "$HOME/.aws/config" file? Try to get 'region' from there
		Map<String, String> awsConfig = parseAwsConfig();
		String regionStr = awsConfig.get(AWS_CONFIG_REGION);

		// Not found? Use the one form the config
		if (regionStr != null) {
			regionStr = regionStr.toUpperCase().replace("-", "_");
		} else {
			regionStr = Config.get().getString(Config.AWS_REGION, DEFAULT_AWS_REGION);
		}

		return Region.getRegion(Regions.valueOf(regionStr.toUpperCase()));
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
			if (localFile != null) this.localPath = localFile;

			S3Object s3object = getS3().getObject(new GetObjectRequest(bucketName, key));
			if (verbose) System.out.println("Downloading '" + this + "'");
			updateInfo(s3object);

			// Create local file and directories
			mkdirsLocal(getLocalPath());
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
	 * Does the directory exist?
	 */
	protected boolean existsDir() {
		ObjectListing objectListing = getS3().listObjects( //
				new ListObjectsRequest() //
						.withBucketName(bucketName) //
						.withPrefix(key) //
						.withMaxKeys(1) // We only need one to check for existence
		);

		// Are there more than zero objects?
		return objectListing.getObjectSummaries().size() > 0;
	}

	@Override
	public String getAbsolutePath() {
		return s3uri.toString();
	}

	public String getBucket() {
		return bucketName;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String getName() {
		if (key == null) return "";

		int idx = key.lastIndexOf('/');
		if (idx >= 0) return key.substring(idx + 1);

		return key;
	}

	@Override
	public String getParent() {
		if (key == null) return AWS_S3_PROTOCOL + bucketName;

		String cp = getAbsolutePath();
		int idx = cp.lastIndexOf('/');
		if (idx >= 0) return cp.substring(0, idx);

		return AWS_S3_PROTOCOL + bucketName;
	}

	@Override
	public String getPath() {
		return bucketName + "/" + key;
	}

	public Region getRegion() {
		return region;
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
					if (region != null) s3.setRegion(region);
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
		return (key == null || key.endsWith("/"));
	}

	@Override
	public boolean isFile() {
		return !isDirectory();
	}

	@Override
	public ArrayList<String> list() {
		ArrayList<String> list = new ArrayList<>();

		// Files are not supposed to have a 'directory' result
		if (isFile()) return list;

		ObjectListing objectListing = getS3().listObjects( //
				new ListObjectsRequest()//
						.withBucketName(bucketName)//
						.withPrefix(key) //
		);

		int keyLen = key.length();
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			String fileName = objectSummary.getKey();
			if (fileName.length() > keyLen) fileName = fileName.substring(keyLen); // First part is not expected in list (only the file name, no prefix)
			list.add(fileName);
		}

		return list;
	}

	@Override
	protected String localPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Config.get().getTmpDir() + "/" + TMP_BDS_DATA + "/s3");

		// Bucket
		for (String part : getBucket().split("/")) {
			if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
		}

		// Key
		if (getKey() != null) {
			for (String part : getKey().split("/")) {
				if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
			}
		}

		return sb.toString();
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
		s3uri = new AmazonS3URI(s3UriStr);
		bucketName = s3uri.getBucket();
		key = s3uri.getKey();

		// Parse and set region
		String regionStr = s3uri.getRegion();
		try {
			if (regionStr != null) region = Region.getRegion(Regions.valueOf(regionStr.toUpperCase()));
			else region = defaultRegion();
		} catch (Exception e) {
			throw new RuntimeException("Cannot parse AWS region '" + regionStr + "'", e);
		}
	}

	/**
	 * Parse "$HOME/.aws/config"
	 * @return A map of key values from AWS config file. An empty map if the file does not exists.
	 */
	Map<String, String> parseAwsConfig() {
		Map<String, String> kv = new HashMap<>();
		if (!Gpr.exists(AWS_CONFIG_FILE)) return kv;

		for (String line : Gpr.readFile(AWS_CONFIG_FILE).split("\n")) {
			String[] fields = line.trim().split("=", 2);
			if (fields.length == 2) {
				String key = fields[0].trim();
				String value = fields[1].trim();
				kv.put(key, value);
			}
		}
		return kv;
	}

	/**
	 * Connect and update info
	 */
	@Override
	protected boolean updateInfo() {
		try {
			if (region == null) {
				String regionName = getS3().getBucketLocation(bucketName);

				// Update region
				if (regionName.equals("US")) region = Region.getRegion(Regions.valueOf(DEFAULT_AWS_REGION));
				else region = Region.getRegion(Regions.fromName(regionName));
			}
		} catch (AmazonClientException e) {
			e.printStackTrace();
			throw new RuntimeException("Error accessing S3 bucket '" + bucketName + "'", e);
		}

		try {
			if (isFile()) {
				S3Object s3object = getS3().getObject(new GetObjectRequest(bucketName, key));
				return updateInfo(s3object);
			} else if (existsDir()) {
				// Special case when keys are 'directories'
				exists = true;
				canRead = true;
				canWrite = true;
				lastModified = new Date(0L);
				size = 0;
				latestUpdate = new Timer(CACHE_TIMEOUT);
				return true;
			} else return false;
		} catch (AmazonServiceException e) {
			String errorCode = e.getErrorCode();
			if (!errorCode.equals("NoSuchKey")) throw new RuntimeException("Error accessing S3 bucket '" + bucketName + "', key '" + key + "'" + this, e);

			// The object does not exists
			exists = false;
			canRead = false;
			canWrite = false;
			lastModified = new Date(0L);
			size = 0;
			latestUpdate = new Timer(CACHE_TIMEOUT);
			return true;
		}
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
		canWrite = true;
		lastModified = om.getLastModified();
		exists = true;
		latestUpdate = new Timer(CACHE_TIMEOUT);

		// Show information
		if (debug) Timer.showStdErr("Updated infromation for '" + this + "'"//
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
		File file = new File(localFileName);
		if (!file.exists() || !file.isFile() || !file.canRead()) {
			if (debug) Gpr.debug("Error accessing local file '" + getLocalPath() + "'");
			return false;
		}

		// Upload
		getS3().putObject(new PutObjectRequest(bucketName, key, file));
		return true;
	}
}
