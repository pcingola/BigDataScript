package org.bds.data;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

import org.bds.Config;
import org.bds.run.BdsThread;
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
 *  ii) Write: We should be able to write to Data
 * iii) Check "last modified": We should be able to
 *      check when the Data was modified
 *
 * Input remote files are downloaded prior to being
 * processed. Output remote files are uploaded after
 * being processed.
 * Note that this download / upload happens in the
 * processing node, which may not be the host running
 * the bds script
 *
 * @author pcingola
 */
public abstract class Data implements Comparable<Data> {

	public static final String PROTOCOL_SEP = "://";
	protected boolean verbose;
	protected boolean debug;
	protected boolean relative; // Is this a relative path? (otherwise is absolute)
	protected String localPath; // File name used for local processing

	public static Data factory(String url) {
		return factory(url, null);
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
		}

		return new Tuple<>(proto, host);
	}

	public Data() {
		verbose = Config.get().isVerbose();
		debug = Config.get().isDebug();
	}

	/**
	 * Is data be executed? (is it an executable file?)
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

	@Override
	public int compareTo(Data d) {
		return toString().compareTo(d.toString());
	}

	/**
	 * Delete data
	 */
	public abstract boolean delete();

	/**
	 * Delete when program finishes
	 */
	public abstract void deleteOnExit();

	/**
	 * Download file to local file system
	 */
	public boolean download() {
		return download(null);
	}

	/**
	 * Download file to local file system
	 */
	public abstract boolean download(Data localFile);

	/**
	 * Does data exists?
	 */
	public abstract boolean exists();

	public abstract String getAbsolutePath();

	public abstract String getCanonicalPath();

	/**
	 * Get latest modification time
	 */
	public abstract Date getLastModified();

	/**
	 * This is a local version of the (possibly remote) data object
	 */
	public String getLocalPath() {
		return localPath;
	}

	public abstract String getName();

	public abstract Data getParent();

	public abstract String getPath();

	/**
	 * Is this a directory (or an equivalent abstraction, such
	 * as an S3 prefix) instead of a data file?
	 */
	public abstract boolean isDirectory();

	/**
	 * Do we have a (valid) local copy of this data?
	 */
	public boolean isDownloaded() {
		return isDownloaded(Data.factory(getLocalPath()));
	}

	/**
	 * Do we have a (valid) local copy of this data?
	 */
	public abstract boolean isDownloaded(Data localFile);

	/**
	 * Does this represent a 'file' (not a directory)
	 */
	public abstract boolean isFile();

	/**
	 * Is this a relative path
	 */
	public boolean isRelative() {
		return relative;
	}

	/**
	 * Is this a remote data object (i.e. not accessible to the file system)
	 */
	public abstract boolean isRemote();

	/**
	 * Do we have a (valid) remote copy of this local data?
	 */
	public boolean isUploaded() {
		return isUploaded(Data.factory(getAbsolutePath()));
	}

	/**
	 * Do we have a (valid) remote copy of this data?
	 */
	public abstract boolean isUploaded(Data localFile);

	/**
	 * Join a segment to this path
	 */
	public abstract Data join(Data segment);

	/**
	 * List of file names under this 'directory'
	 * Data objects are resolved respect to the base
	 * data object requesting the list().
	 * For instance, remote objects must contain the
	 * full information to find /retrienve the object
	 * (such as protocol, server, path, etc.) and not
	 * just the 'file name'
	 */
	public abstract ArrayList<Data> list();

	/**
	 * Create all directories in path
	 */
	public abstract boolean mkdirs();

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Data object size
	 */
	public abstract long size();

	/**
	 * By default 'toString' shows the "god representation" of
	 * the Data path:
	 * - If the data is a local file, the absolute path is shown
	 * - If the data is remote, the full URL/URL should be shown
	 * No attempt to resolve the name should be made (e.g. do
	 * not try to resolve 'canonical' paths)
	 */
	@Override
	public String toString() {
		throw new RuntimeException("Unimplemented method: You must implement Data.toString() for custom data types. This should never happen!");
	}

	/**
	 * Upload local version of the file to remote file system
	 */
	public boolean upload() {
		return upload(Data.factory(getLocalPath()));
	}

	/**
	 * Upload a file to location
	 * @return true if successful
	 */
	public abstract boolean upload(Data locaLFile);

}
