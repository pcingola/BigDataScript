package ca.mcgill.mcb.pcingola.bigDataScript.data;

import java.util.ArrayList;
import java.util.Date;

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

	DataScheme scheme; // File system type
	String path;
	String localPath; // File name used for local processing

	public static Data factory(String url) {
		return factory(url, null);
	}

	/**
	 * Create a data object, resolve local files using 'currentDir'
	 */
	public static Data factory(String url, String currentDir) {
		DataScheme scheme = DataScheme.parse(url);
		if (scheme == null) throw new RuntimeException("Unknown data scheme '" + url + "'");

		switch (scheme) {
		case FILE:
			return new DataFile(url, currentDir);

		default:
			throw new RuntimeException("Unimplemented scheme '" + scheme + "'");
		}
	}

	/**
	 * Remove scheme part form URL
	 */
	public static String removeScheme(String url, DataScheme scheme) {
		String schemePrefix = scheme + DataScheme.SEP;
		if (url.toUpperCase().startsWith(schemePrefix)) url = url.substring(schemePrefix.length());
		return url;
	}

	public Data(String url) {
		// Parse scheme
		scheme = DataScheme.parse(url);

		// Default scheme: Local file
		path = removeScheme(url, scheme);
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
	public abstract boolean download();

	/**
	 * Does data exists?
	 */
	public abstract boolean exists();

	/**
	 * Get latest medification time
	 */
	public abstract Date getLastModified();

	/**
	 * This is a local version of the (possibly remote) data object
	 */
	public String getLocalPath() {
		return localPath;
	}

	public String getName() {
		int idx = path.lastIndexOf('/');
		if (idx >= 0) return path.substring(idx);
		return path;
	}

	public abstract String getParent();

	public String getPath() {
		return path;
	}

	public DataScheme getScheme() {
		return scheme;
	}

	public String getUrl() {
		if (scheme.isRemote()) return scheme + DataScheme.SEP + path;
		return path;
	}

	/**
	 * Is this a directory (or an equivalent abstraction, such
	 * as an S3 bucket / path) instead of a data file?
	 */
	public abstract boolean isDirectory();

	/**
	 * Do we have a (valid) local copy of this data?
	 */
	public abstract boolean isDownloaded();

	/**
	 * Does thi represent a 'file' (not a directory)
	 */
	public abstract boolean isFile();

	/**
	 * List of file names under this 'directory'
	 * File names should be returned as canonical paths
	 */
	public abstract ArrayList<String> list();

	public abstract boolean mkdirs();

	/**
	 * Data object size
	 */
	public abstract long size();

	@Override
	public String toString() {
		return getUrl();
	}

	/**
	 * Upload local version of the file to remote file system
	 */
	public abstract boolean upload();
}
