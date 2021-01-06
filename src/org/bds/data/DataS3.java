package org.bds.data;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.bds.Config;
import org.bds.util.Gpr;
import org.bds.util.Timer;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
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

	private static final long serialVersionUID = -815001806425931041L;

	public static final String AWS_S3_VIRTUAL_HOSTED_SCHEME = "https"; // In AWS lingo referring to an object "virtual hosted style" is something like 'https://bucket-name.s3.Region.amazonaws.com/key_name"
	public static final String AWS_S3_VIRTUAL_HOSTED_DOMAIN = "amazonaws.com";
	public static final String AWS_S3_PROTOCOL = "s3";
	public static final String AWS_S3_REGION = "awsS3Region";

	protected String region;
	protected String bucketName;
	protected String key;

	public DataS3(String urlStr, String region) {
		super(urlStr, DataType.S3);
		this.region = region;
		parseS3Uri(urlStr); // Note: If the region is in the URL, it will be overridden
		canWrite = false;
	}

	public DataS3(URI uri, String region) {
		super(uri.toString(), DataType.S3);
		canWrite = false;
		this.region = region;
		parseS3Uri(uri); // Note: If the region is in the URL, it will be overridden
	}

	/**
	 * Can this data be specified in 's3://' URL format:
	 *
	 * 		s3://bucket/key
	 *
	 * Note that the `s3://` format does not specify the region
	 */
	protected boolean canBeS3Format() {
		return (uri == null || uri.getScheme().equals(AWS_S3_PROTOCOL)) //
				&& (region == null || region.isEmpty());
	}

	@Override
	public String createUrl() {
		return canBeS3Format() ? urlS3() : urlHttps();
	}

	@Override
	public boolean deleteRemote() {
		if (!isFile()) return false; // Do not delete bucket or prefix
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(key).build();
		getS3Client().deleteObject(deleteObjectRequest);
		return true;
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
			getS3Client().getObject(req, ResponseTransformer.toFile(Paths.get(localPath)));
			log("Donwload from '" + toString() + "' to '" + localPath + "' finished.");

			// Update last modified info
			updateLocalFileLastModified();

			return true;
		} catch (Exception e) {
			error("Error while downloading " + this);
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

		if (canBeS3Format()) return new DataS3("s3://" + bucketName + '/' + keyParen, region);
		return new DataS3(AWS_S3_VIRTUAL_HOSTED_SCHEME + "://" + bucketName + ".s3." + region + "." + AWS_S3_VIRTUAL_HOSTED_DOMAIN + "/" + keyParen, region);
	}

	@Override
	public String getPath() {
		return (key != null ? '/' + key : "");
	}

	public String getRegion() {
		return region;
	}

	/**
	 * Create an S3 client.
	 * S3 clients are thread safe, thus it is encouraged to have
	 * only one client instead of instantiating one each time.
	 */
	protected S3Client getS3Client() {
		return AwsS3ClientProvider.get(region);
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
	 * Is this data specified in 's3://' URL format:
	 *
	 * 		s3://bucket/key
	 *
	 * Note that the `s3://` format does not specify the region
	 *
	 * Reference: https://docs.aws.amazon.com/AmazonS3/latest/dev/UsingBucket.html#access-bucket-intro
	 */
	protected boolean isS3Format() {
		return uri != null && uri.getScheme().equals(AWS_S3_PROTOCOL);
	}

	/**
	 * Is this data specified in 'virtual host' URL format?
	 *
	 * In AWS lingo referring to an object "virtual hosted style" is something like:
	 *
	 * 		https://bucket-name.s3.Region.amazonaws.com/key_name
	 *
	 * instead of the "typical" format:
	 * 		s3://bucket/key
	 *
	 * Note that the `s3://` format does not specify the region
	 *
	 * Reference: https://docs.aws.amazon.com/AmazonS3/latest/dev/UsingBucket.html#access-bucket-intro
	 */
	protected boolean isVirtualHostFormat() {
		return uri != null && uri.getScheme().equals(AWS_S3_VIRTUAL_HOSTED_SCHEME);
	}

	/**
	 * Join a segment to this path
	 */
	@Override
	public Data join(Data segment) {
		File fpath = new File(getPath());
		File fjoin = new File(fpath, segment.getPath());
		String s3uriStr = "s3://" + bucketName + fjoin.getAbsolutePath();
		return new DataS3(s3uriStr, region);
	}

	@Override
	public ArrayList<Data> list() {
		ArrayList<Data> list = new ArrayList<>();

		try {
			// Files are not supposed to have a 'directory' result
			if (isFile()) return list;

			for (S3Object s3obj : listObjects()) {
				String s3path = urlRoot() + s3obj.key();
				list.add(new DataS3(s3path, region));
			}
		} catch (Exception e) {
			error("Error while listing files from '" + this + "'");
		}

		return list;
	}

	/**
	 * List objects in bucket/key
	 * @return A list of S3Objects
	 */
	List<S3Object> listObjects() {
		ListObjectsRequest req = ListObjectsRequest.builder().bucket(bucketName).prefix(key).build();
		ListObjectsResponse objectListing = getS3Client().listObjects(req);
		return objectListing.contents();
	}

	/**
	 * List only one object in bucket/key
	 * @return An S3Object or null
	 */
	S3Object listOneObject() {
		ListObjectsRequest req = ListObjectsRequest.builder().bucket(bucketName).prefix(key).maxKeys(1).build();
		ListObjectsResponse objectListing = getS3Client().listObjects(req);
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

	protected void parseS3Uri(String uriStr) {
		try {
			uri = new URI(uriStr);
			parseS3Uri(uri);
		} catch (URISyntaxException e) {
			// Could not parse URI
			bucketName = "";
			key = uriStr;
		}
	}

	protected void parseS3Uri(URI uri) {
		String host = uri.getHost();
		if (uri.getScheme().equals(AWS_S3_VIRTUAL_HOSTED_SCHEME) && host.endsWith(AWS_S3_VIRTUAL_HOSTED_DOMAIN)) {
			// Virtual hosted style, the URI is:
			//     https://bucket-name.s3.Region.amazonaws.com/key_name
			String[] parts = host.split("\\.");
			if (parts.length != 5 //
					|| !parts[1].equals("s3") //
					|| !parts[3].equals("amazonaws") //
					|| !parts[4].equals("com") //
			) throw new RuntimeException("Could not parse S3 URI '" + uri + "', the format should 'https://bucket-name.s3.Region.amazonaws.com/key_name'");
			bucketName = parts[0];
			region = parts[2];

			key = uri.getPath();
			if (key.startsWith("/")) key = key.substring(1);
		} else if (uri.getScheme().equals(AWS_S3_PROTOCOL)) {
			bucketName = uri.getAuthority();
			key = uri.getPath();
			if (key.startsWith("/")) key = key.substring(1);
		} else {
			throw new RuntimeException("Could not parse S3 URI '" + uri + "', the format should be either 's3://bucket/key_name' or 'https://bucket-name.s3.Region.amazonaws.com/key_name'");
		}
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
		lastModified = java.util.Date.from(s3object.lastModified());
		exists = true;
		latestUpdate = new Timer(CACHE_TIMEOUT);

		// Show information
		debug("Updated infromation for '" + this + "'"//
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
			debug("Error accessing local file '" + getLocalPath() + "'");
			return false;
		}

		// Upload
		File localFile = new File(local.getAbsolutePath());
		PutObjectRequest req = PutObjectRequest.builder().bucket(bucketName).key(key).build();
		getS3Client().putObject(req, RequestBody.fromFile(localFile));
		return true;
	}

	/**
	 * URL in "https://" format (virtual host format)
	 */
	public String urlHttps() {
		return AWS_S3_VIRTUAL_HOSTED_SCHEME + "://" + bucketName + ".s3." + region + "." + AWS_S3_VIRTUAL_HOSTED_DOMAIN + '/' + key;
	}

	/**
	 * Construct the 'root' URL for this object
	 * Note: Since this is a 'root' directory, it always ends with a trailing '/'
	 */
	protected String urlRoot() {
		if (isS3Format()) return AWS_S3_PROTOCOL + "://" + bucketName + '/';
		return AWS_S3_VIRTUAL_HOSTED_SCHEME + "://" + bucketName + ".s3." + region + "." + AWS_S3_VIRTUAL_HOSTED_DOMAIN + '/';
	}

	/**
	 * URL in "s3://" format
	 */
	public String urlS3() {
		return AWS_S3_PROTOCOL + "://" + bucketName + '/' + key;
	}

}
