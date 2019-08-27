package org.bds.data;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

import org.bds.Config;

/**
 * A data object: Typically a file, but
 * could be other things as well (an object stored
 * in S3, data file in an HTTP or FTP server, etc.)
 *
 * There are only a few minimal primitives we need
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
public abstract class Data {

	protected boolean verbose;
	protected boolean debug;
	protected boolean relative; // Is this a relative path? (otherwise is absolute)
	protected String localPath; // File name used for local processing

	public static final String PROTOCOL_SEP = "://";

	public static Data factory(String url) {
		return factory(url, null);
	}

	/**
	 * Create a data object, resolve local files using 'currentDir'
	 */
	public static Data factory(String urlStr, String currentDir) {
		String proto = "file";

		// Get protocol
		int idx = urlStr.indexOf(PROTOCOL_SEP);
		if (idx < 0) proto = "file";
		else {
			proto = urlStr.substring(0, idx);
			String host = urlStr.substring(idx + PROTOCOL_SEP.length());

			// Check is this is an S3 URL
			// Get host
			int idxHost = host.indexOf('/');
			if (idxHost > 0) host = host.substring(0, idxHost);

			int idxPort = host.indexOf(':');
			if (idxPort > 0) host = host.substring(0, idxPort);

			if (host.endsWith(DataS3.AWS_DOMAIN)) {
				String s3domain = host.substring(0, host.length() - DataS3.AWS_DOMAIN.length() - 1);
				int idxDot = s3domain.lastIndexOf('.');
				if (idxDot > 0) s3domain = s3domain.substring(idxDot + 1);
				if (s3domain.startsWith(DataS3.AWS_S3_PREFIX)) proto = "s3";
			}
		}

		// Create each data type
		Data data = null;
		switch (proto) {
		case "file":
			data = new DataFile(urlStr, currentDir);
			break;

		case "http":
		case "https":
			data = new DataHttp(urlStr);
			break;

		case "s3":
			data = new DataS3(urlStr);
			break;

		case "ftp":
			data = new DataFtp(urlStr);
			break;

		case "sftp":
			data = new DataSftp(urlStr);
			break;

		default:
			throw new RuntimeException("Unimplemented protocol '" + proto + "' for URL " + urlStr);
		}

		// Set values form config
		data.setVerbose(Config.get().isVerbose());
		data.setDebug(Config.get().isDebug());

		return data;
	}

	public static Data factory(URI uri) {
		// Create each data type
		Data data = null;
		String proto = uri.getScheme();
		switch (proto) {
		case "file":
			data = new DataFile(uri);
			break;

		case "http":
		case "https":
			data = new DataHttp(uri);
			break;

		case "s3":
			data = new DataS3(uri);
			break;

		case "ftp":
			data = new DataFtp(uri);
			break;

		case "sftp":
			data = new DataSftp(uri);
			break;

		default:
			throw new RuntimeException("Unimplemented protocol '" + proto + "' for URL " + uri);
		}

		// Set values form config
		data.setVerbose(Config.get().isVerbose());
		data.setDebug(Config.get().isDebug());

		return data;
	}

	public Data() {
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
	public abstract boolean download(String localFileName);

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

	public abstract String getParent();

	public abstract String getPath();

	public abstract URI getUri();

	/**
	 * Is this a directory (or an equivalent abstraction, such
	 * as an S3 bucket / path) instead of a data file?
	 */
	public abstract boolean isDirectory();

	/**
	 * Do we have a (valid) local copy of this data?
	 */
	public boolean isDownloaded() {
		return isDownloaded(getLocalPath());
	}

	/**
	 * Do we have a (valid) local copy of this data?
	 */
	public abstract boolean isDownloaded(String localPath);

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
		return isUploaded(getLocalPath());
	}

	/**
	 * Do we have a (valid) remote copy of this data?
	 */
	public abstract boolean isUploaded(String localPath);

	/**
	 * Join a segment to this path
	 */
	public abstract Data join(Data segment);

	/**
	 * List of file names under this 'directory'
	 * File names should be returned as canonical paths
	 */
	public abstract ArrayList<String> list();

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
	 * Upload local version of the file to remote file system
	 */
	public boolean upload() {
		return upload(getLocalPath());
	}

	/**
	 * Upload a file to location
	 * @return true if successful
	 */
	public abstract boolean upload(String locaLFileName);

}
