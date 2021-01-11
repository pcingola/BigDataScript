package org.bds.data;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

import org.bds.BdsLog;
import org.bds.Config;
import org.bds.run.BdsThread;
import org.bds.run.BdsThreads;
import org.bds.scope.GlobalScope;
import org.bds.util.Tuple;

/**
 * A data object: Typically a file, but
 * could be other things as well (an object stored
 * in S3, data file in an HTTP or FTP server, etc.)
 *
 * There are only a few primitives we need
 * to be able to satisfy:
 *
 *   i) Read: We should be able to read Data
 *
 *  ii) Write: We should be able to write to Data
 *
 * iii) Check "last modified": We should be able to
 *      check when the Data was modified
 *
 * Input remote files are downloaded prior to being
 * processed. Output remote files are uploaded after
 * being processed.
 *
 * Note that this download / upload happens in the
 * processing node, which may not be the host running
 * the bds script
 *
 * @author pcingola
 */
public abstract class Data implements Comparable<Data>, Serializable, BdsLog {

	private static final long serialVersionUID = 6436509779796130272L;
	public static final String PROTOCOL_SEP = "://";

	protected boolean verbose;
	protected boolean debug;
	protected boolean relative; // Is this a relative path? (otherwise is absolute)
	protected String localPath; // File name used for local processing
	protected String urlOri; // URL in the original representation
	protected String urlStr; // URL (full representation)
	protected final DataType dataType; // File type

	/**
	 * Get canonical path to file using thread's 'current dir' to de-reference
	 * relative paths
	 *
	 * Warning: When un-serializing a task form a checkpoint, threads are not
	 *          initialized, thus they are null
	 */
	public static Data factory(String url) {
		BdsThread bdsThread = BdsThreads.getInstance().getOrRoot(); // Can be null
		return factory(url, bdsThread);
	}

	/**
	 * Create a data object, resolve local files using 'currentDir'
	 */
	public static Data factory(String urlStr, BdsThread bdsThread) {
		// Get protocol
		Tuple<String, String> protoHost = parseProtoHost(urlStr);
		String proto = protoHost.first;

		switch (proto) {
		case "file":
			String currDir = (bdsThread != null ? bdsThread.getCurrentDir() : null);
			return new DataFile(urlStr, currDir);

		case "http":
		case "https":
			return new DataHttp(urlStr);

		case "s3":
			String awsRegion = (bdsThread != null ? bdsThread.getValue(GlobalScope.GLOBAL_VAR_AWS_REGION).asString() : "");
			return new DataS3(urlStr, awsRegion);

		case "ftp":
			return new DataFtp(urlStr);

		case "sftp":
			return new DataSftp(urlStr);

		default:
			throw new RuntimeException("Unimplemented protocol '" + proto + "' for URL " + urlStr);
		}
	}

	/**
	 * Create a data object from URI
	 */
	public static Data factory(URI uri, BdsThread bdsThread) {
		// Create each data type
		String proto = uri.getScheme();

		// Is this an S3 object in 'virtual hosted' style?
		if (uri.getScheme() != null //
				&& uri.getHost() != null //
				&& uri.getScheme().equals(DataS3.AWS_S3_VIRTUAL_HOSTED_SCHEME) //
				&& uri.getHost().endsWith(DataS3.AWS_S3_VIRTUAL_HOSTED_DOMAIN) //
		) proto = DataS3.AWS_S3_PROTOCOL;

		switch (proto) {
		case "file":
			return new DataFile(uri);

		case "http":
		case "https":
			return new DataHttp(uri);

		case "s3":
			String awsRegion = (bdsThread != null ? bdsThread.getValue(GlobalScope.GLOBAL_VAR_AWS_REGION).asString() : "");
			return new DataS3(uri, awsRegion);

		case "ftp":
			return new DataFtp(uri);

		case "sftp":
			return new DataSftp(uri);

		default:
			throw new RuntimeException("Unimplemented protocol '" + proto + "' for URL " + uri);
		}
	}

	/**
	 * Parse 'protocol' and 'host' from URL string
	 * @return A tuple with <proto, host> strings
	 */
	public static Tuple<String, String> parseProtoHost(String urlStr) {
		String proto = "file";
		String host = null;
		int idx = urlStr.indexOf(PROTOCOL_SEP);
		if (idx >= 0) {
			proto = urlStr.substring(0, idx).toLowerCase();

			// Check is this is an S3 URL
			// Get host
			host = urlStr.substring(idx + PROTOCOL_SEP.length());
			int idxHost = host.indexOf('/');
			if (idxHost > 0) host = host.substring(0, idxHost);

			int idxPort = host.indexOf(':');
			if (idxPort > 0) host = host.substring(0, idxPort);

			// S3 can be addressed by host ending with "amazonaws.com":
			//     https://bucket-name.s3.Region.amazonaws.com/key name
			// Reference: https://docs.aws.amazon.com/AmazonS3/latest/dev/UsingBucket.html#access-bucket-intro
			if (proto.equals(DataS3.AWS_S3_VIRTUAL_HOSTED_SCHEME) && host.endsWith(DataS3.AWS_S3_VIRTUAL_HOSTED_DOMAIN)) proto = DataS3.AWS_S3_PROTOCOL;
		}

		return new Tuple<>(proto, host);
	}

	public Data(String urlOri, DataType dataType) {
		this.urlOri = urlOri;
		verbose = Config.get().isVerbose();
		debug = Config.get().isDebug();
		this.dataType = dataType;
	}

	/**
	 * Is it an executable file?
	 */
	public abstract boolean canExecute();

	/**
	 * Is data readable?
	 */
	public abstract boolean canRead();

	/**
	 * Is data writable?
	 */
	public abstract boolean canWrite();

	/**
	 * Are these data the same?
	 */
	@Override
	public int compareTo(Data d) {
		return toString().compareTo(d.toString());
	}

	/**
	 * Build a URL that defines this data file.
	 *
	 * This string must include all elements required in the URL, such as
	 * protocol, port, etc.
	 *
	 * Paths should be canonical whenever possible.
	 *
	 * If the full information is not available, this method must return 'null'
	 * (e.g. if this is a 'relative' data file)
	 *
	 * @return URL defining the path to the data file or 'null' if relevant data is not available
	 */
	protected abstract String createUrl();

	/**
	 * Delete data file
	 * Note: Some data types might not accept deletion (e.g. HTTP)
	 *
	 * @return True on success
	 */
	public abstract boolean delete();

	/**
	 * Delete this data file when the bds program finishes
	 *
	 * Note: This cannot always be waranteed and should be considered as "best effort"
	 *       For instance, if the program is killed, or the host shutdown, then we
	 *       won't be able to clean up the files
	 */
	public abstract void deleteOnExit();

	/**
	 * Download (remote) file to local file system
	 * Note: It has no effect on local files
	 * @return True on success
	 */
	public boolean download() {
		return download(null);
	}

	/**
	 * Download (remote) file to a specific location in the local file system
	 * Note: It it always fails for local files
	 * @return True on success
	 */
	public abstract boolean download(Data localFile);

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o.getClass().equals(this.getClass()))) return false;
		return this.url().equals(((Data) o).url());
	}

	/**
	 * Does data file exists?
	 */
	public abstract boolean exists();

	/**
	 * Return the absolute path to the file
	 * E.g. The path from the root directory
	 *
	 * Note: For remote files it must ONLY include the path and never
	 *       the protocol, host, or any other information
	 *
	 * @return Absolute path
	 */
	public abstract String getAbsolutePath();

	/**
	 * Return the canonical path to the file.
	 * E.g.: The path from the root directory with all symbolic links resolved
	 *
	 * Note: For remote files it must ONLY include the path and never
	 *       the protocol, host, or any other information
	 *
	 * @return Canonical path
	 */
	public abstract String getCanonicalPath();

	public DataType getDataType() {
		return dataType;
	}

	/**
	 * Get last time this file was modified
	 *
	 * Note: In some remote data files this might not be available
	 *       Even for local file systems might only provide day or
	 *       hour granularity (if a file is modified many times some
	 *       file systems only store one change per day to save time
	 *       and reduce file system load.
	 */
	public abstract Date getLastModified();

	/**
	 * This is a local version of the (possibly remote) data object
	 */
	public String getLocalPath() {
		return localPath;
	}

	public abstract String getName();

	/**
	 * The the parent directory for the file
	 *
	 * Note: This returns a Data object pointing to the parent directory.
	 *
	 * @return Data file's parent directory
	 */
	public abstract Data getParent();

	/**
	 * Get the path to the data file
	 *
	 * Note: For remote files it must ONLY include the path and never
	 *       the protocol, host, or any other information
	 *
	 * @return Path to file, could be an empty string if the file's
	 *         path was not specified
	 */
	public abstract String getPath();

	/**
	 * Shows the "god representation" of the Data:
	 * - If the data is a local file, the absolute path is shown
	 * - If the data is remote, the full URL/URL should be shown
	 * No attempt to resolve the name should be made (e.g. do
	 * not try to resolve 'canonical' paths)
	 */
	public String getPathOrUrl() {
		return isRemote() ? url() : getAbsolutePath();
	}

	/**
	 * Get the original string that was used to define the data
	 * file, without any alterations or parsing
	 *
	 * @return Original string that defined this data file
	 */
	public String getUrlOri() {
		return urlOri;
	}

	@Override
	public int hashCode() {
		return url().hashCode();
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Is this a directory (or an equivalent abstraction, such
	 * as an S3 prefix) instead of a data file?
	 */
	public abstract boolean isDirectory();

	/**
	 * Do we have a (valid) local copy of this data?
	 */
	public boolean isDownloaded() {
		return isDownloaded(factory(getLocalPath()));
	}

	/**
	 * Do we have a (valid) local copy of this data at 'localFile'?
	 */
	public abstract boolean isDownloaded(Data localFile);

	/**
	 * Does this represent a 'file' (i.e. not a directory)
	 */
	public abstract boolean isFile();

	/**
	 * Is this a relative path?
	 *
	 * Note: If a path is 'relative', it might be part of a remote
	 *       data file, even though it is a 'DataFile' which often
	 *       refer to local file-system
	 */
	public boolean isRelative() {
		return relative;
	}

	/**
	 * Is this a remote data object?
	 * E.g.: http, ftp, S3, etc.
	 */
	public abstract boolean isRemote();

	/**
	 * Do we have a (valid) remote copy of the local file data?
	 * E.g. The process modified a local copy of the file and we want
	 *      to upload the results if they haven't been already uploaded.
	 */
	public boolean isUploaded() {
		return isUploaded(factory(getAbsolutePath()));
	}

	/**
	 * Do we have a (valid) remote copy of the data in 'localFile'?
	 * E.g. The process modified a local copy of the file and we want
	 *      to upload the results if they haven't been already uploaded.
	 */
	public abstract boolean isUploaded(Data localFile);

	@Override
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Join a 'segment' to this path
	 * Typically 'segment' is a relative path
	 */
	public abstract Data join(Data segment);

	/**
	 * List of file names under this 'directory'
	 *
	 * Data objects are resolved respect to the base
	 * data object requesting the list().
	 *
	 * For instance, remote objects must contain the
	 * full information to find /retrienve the object
	 * (such as protocol, server, path, etc.) and not
	 * just the 'file name'
	 */
	public abstract ArrayList<Data> list();

	/**
	 * Create directory and all intermediate directories in path
	 */
	public abstract boolean mkdirs();

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Data object size in bytes
	 */
	public abstract long size();

	/**
	 * Returns the original representation of the data file
	 * without any changes.
	 * No attempt to resolve the name should be made (e.g. do
	 * not try to resolve 'canonical' or provide 'absolute' paths)
	 */
	@Override
	public String toString() {
		return urlOri;
	}

	/**
	 * Upload local version of the file to remote file system
	 */
	public boolean upload() {
		return upload(factory(getLocalPath()));
	}

	/**
	 * Upload a file to location
	 * @return true if successful
	 */
	public abstract boolean upload(Data locaLFile);

	/**
	 * Create a full canonical URL to identify this object.
	 * WARNING: This method is used as the base for 'Data.equals' and 'Data.hashCode'.
	 *
	 * @return A string representing a canonical URL to this data
	 */
	public String url() {
		if (urlStr == null) urlStr = createUrl();
		return urlStr;
	};

}
