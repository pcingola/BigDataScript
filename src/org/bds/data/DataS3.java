package org.bds.data;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.bds.Config;
import org.bds.util.Gpr;
import org.bds.util.Timer;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.http.apache.ProxyConfiguration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

/**
 * A bucket / object in AWS S3
 *
 * @author pcingola
 */
public class DataS3 extends DataRemote {

	public static final String AWS_DOMAIN = "amazonaws.com";
	public static final String AWS_S3_PROTOCOL = "s3";

	public static final String ENV_PROXY_HTTTP = "http_proxy";
	public static final String ENV_PROXY_HTTTPS = "https_proxy";
	protected transient S3Client s3;
	protected String bucketName;
	protected String key;

	public DataS3(String urlStr) {
		super();
		parseS3Uri(urlStr);
		canWrite = false;
	}

	public DataS3(URI uri) {
		super();
		canWrite = false;
		parseS3Uri(uri);
	}

	@Override
	public boolean delete() {
		if (!isFile()) return false; // Do not delete bucket
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(key).build();
		getS3().deleteObject(deleteObjectRequest);
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
	public boolean download(Data local) {
		try {
			if (!isFile()) return false;
			if (local != null) localPath = local.getAbsolutePath();
			else if (localPath == null) localPath = getLocalPath();

			File localFile = new File(localPath);

			// Make sure the parent directory exists
			File parent = localFile.getParentFile();
			if (parent != null) parent.mkdirs();

			// Remove local file (if it already exists)
			localFile.delete();

			// Download from S3
			GetObjectRequest req = GetObjectRequest.builder().bucket(bucketName).key(key).build();
			getS3().getObject(req, ResponseTransformer.toFile(Paths.get(localPath)));
			if (verbose) Timer.showStdErr("Donwload from '" + toString() + "' to '" + localPath + "' finished.");

			// Update last modified info
			updateLocalFileLastModified();

			return true;
		} catch (Exception e) {
			Timer.showStdErr("ERROR while downloading " + this);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Does the directory exist?
	 */
	protected boolean existsDir() {
		return listOneObject() != null;
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
	public Data getParent() {
		String keyParen = "";
		int idx = key.lastIndexOf('/');
		if (idx >= 0) keyParen = key.substring(0, idx);
		return new DataS3("s3://" + bucketName + '/' + keyParen);
	}

	@Override
	public String getPath() {
		return (key != null ? '/' + key : "");
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
	protected S3Client getS3() {
		if (s3 == null) {
			URL proxyUrl = getProxyFromEnv();

			// Do we have proxy information?
			if (proxyUrl == null) {
				// No proxy? Use default client
				s3 = S3Client.create();
			} else {
				// Set proxy in config
				//				ClientConfiguration config = new ClientConfiguration();
				//				config.setProxyHost(proxyUrl.getHost());
				//				config.setProxyPort(proxyUrl.getPort());
				//				s3 = S3Client.builder().standard().withClientConfiguration(config).build();
				ProxyConfiguration proxyConf = ProxyConfiguration.builder().useSystemPropertyValues(true).build();
				final SdkHttpClient httpClient = ApacheHttpClient.builder().proxyConfiguration(proxyConf).build();
				s3 = S3Client.builder().httpClient(httpClient).build();
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

	/**
	 * Join a segment to this path
	 */
	@Override
	public Data join(Data segment) {
		File fpath = new File(getPath());
		File fjoin = new File(fpath, segment.getPath());
		String s3uriStr = "s3://" + bucketName + fjoin.getAbsolutePath();
		return factory(s3uriStr);
	}

	@Override
	public ArrayList<Data> list() {
		ArrayList<Data> list = new ArrayList<>();

		try {
			// Files are not supposed to have a 'directory' result
			if (isFile()) return list;

			for (S3Object s3obj : listObjects()) {
				String s3path = AWS_S3_PROTOCOL + "://" + bucketName + '/' + s3obj.key();
				list.add(new DataS3(s3path));
			}
		} catch (Exception e) {
			if (verbose) Timer.showStdErr("ERROR while listing files from '" + this + "'");
		}

		return list;
	}

	/**
	 * List objects in bucket/key
	 * @return A list of S3Objects
	 */
	List<S3Object> listObjects() {
		ListObjectsRequest req = ListObjectsRequest.builder().bucket(bucketName).prefix(key).build();
		ListObjectsResponse objectListing = getS3().listObjects(req);
		return objectListing.contents();
	}

	/**
	 * List only one object in bucket/key
	 * @return An S3Object or null
	 */
	S3Object listOneObject() {
		ListObjectsRequest req = ListObjectsRequest.builder().bucket(bucketName).prefix(key).maxKeys(1).build();
		ListObjectsResponse objectListing = getS3().listObjects(req);
		return objectListing.hasContents() ? objectListing.contents().get(0) : null;
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

	protected void parseS3Uri(String urlStr) {
		try {
			parseS3Uri(new URI(urlStr));
		} catch (URISyntaxException e) {
			// Could not parse URI
			bucketName = "";
			key = urlStr;
		}
	}

	protected void parseS3Uri(URI uri) {
		bucketName = uri.getAuthority();
		key = uri.getPath();
		if (key.startsWith("/")) key = key.substring(1);
	}

	@Override
	public String toString() {
		return AWS_S3_PROTOCOL + "://" + bucketName + "/" + key;
	}

	/**
	 * Connect and update info
	 */
	@Override
	protected boolean updateInfo() {
		if (isFile()) {
			S3Object s3object = listOneObject();
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
	}

	/**
	 * Update object's information
	 */
	protected boolean updateInfo(S3Object s3object) {
		// Update data
		if (s3object == null) return false;
		size = s3object.size();
		canRead = true;
		canWrite = true;
		lastModified = Date.from(s3object.lastModified());
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
	public boolean upload(Data local) {
		// Create and check file
		if (!local.exists() || !local.isFile() || !local.canRead()) {
			if (debug) Gpr.debug("Error accessing local file '" + getLocalPath() + "'");
			return false;
		}

		// Upload
		File localFile = new File(local.getAbsolutePath());
		PutObjectRequest req = PutObjectRequest.builder().bucket(bucketName).key(key).build();
		getS3().putObject(req, RequestBody.fromFile(localFile));
		return true;
	}

}
