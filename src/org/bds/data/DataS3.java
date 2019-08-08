package org.bds.data;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;

import org.bds.Config;
import org.bds.util.Gpr;
import org.bds.util.Timer;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
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

	public static final String AWS_DOMAIN = "amazonaws.com";
	public static final String AWS_S3_PREFIX = "s3";
	public static final String AWS_S3_PROTOCOL = "s3://";

	public static final String ENV_PROXY_HTTTP = "http_proxy";
	public static final String ENV_PROXY_HTTTPS = "https_proxy";

	protected AmazonS3 s3;
	protected AmazonS3URI s3uri;
	protected String bucketName;
	protected String key;

	public DataS3(String urlStr) {
		super();
		parseUrlS3(urlStr);
		canWrite = false;
	}

	public DataS3(URI baseUri, File path) {
		super();
		uri = replacePath(baseUri, path);
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

	/**
	 * Get proxy from environment variables
	 */
	protected URL getProxyFromEnv() {
		String proxy = System.getenv(ENV_PROXY_HTTTPS);
		if (proxy == null) proxy = System.getenv(ENV_PROXY_HTTTP);

		URL proxyUrl = null;
		try {
			if (proxy != null) proxyUrl = new URL(proxy);
		} catch (MalformedURLException e) {
			Gpr.debug("Error parsing proxy from environment '" + proxy + "', ignoring");
		}

		return proxyUrl;
	}

	/**
	 * Create an S3 client.
	 * S3 clients are thread safe, thus it is encouraged to have
	 * only one client instead of instantiating one each time.
	 */
	protected AmazonS3 getS3() {
		if (s3 == null) {
			URL proxyUrl = getProxyFromEnv();

			// Do we have proxy information?
			if (proxyUrl == null) {
				// No proxy? Use default client
				s3 = AmazonS3ClientBuilder.defaultClient();
			} else {
				// Set proxy in config
				ClientConfiguration config = new ClientConfiguration();
				config.setProxyHost(proxyUrl.getHost());
				config.setProxyPort(proxyUrl.getPort());
				s3 = AmazonS3ClientBuilder.standard().withClientConfiguration(config).build();
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
	public DataS3 join(String segment) {
		File f = new File(getAbsolutePath(), segment);
		return new DataS3(uri, f);
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
			// First part is not expected in list (only the file name, no prefix)
			if (fileName.length() > keyLen) fileName = fileName.substring(keyLen);
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
	protected void parseUrlS3(String s3UriStr) {
		s3uri = new AmazonS3URI(s3UriStr);
		bucketName = s3uri.getBucket();
		key = s3uri.getKey();
	}

	/**
	 * Connect and update info
	 */
	@Override
	protected boolean updateInfo() {
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
