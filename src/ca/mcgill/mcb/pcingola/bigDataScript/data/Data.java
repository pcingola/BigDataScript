package ca.mcgill.mcb.pcingola.bigDataScript.data;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.utils.URIBuilder;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;

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

	public static final String PROTOCOL_SEP = "://";

	protected boolean verbose;
	protected boolean debug;
	protected String localPath; // File name used for local processing

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
		else proto = urlStr.substring(0, idx);

		// Create each data type
		Data data = null;
		switch (proto) {
		case "file":
			data = new DataFile(urlStr, currentDir);
			break;

		case "http":
		case "https":
			data = new DataHttp(parseUrl(urlStr));
			break;

		case "s3":
			data = new DataS3(parseUrl(urlStr));
			break;

		default:
			throw new RuntimeException("Unimplemented proteocol '" + proto + "' for URL " + urlStr);
		}

		// Set values form config
		data.setVerbose(Config.get().isVerbose());
		data.setDebug(Config.get().isDebug());

		return data;
	}

	public static URL parseUrl(String urlStr) {
		try {
			// No protocol: file
			if (urlStr.indexOf(PROTOCOL_SEP) < 0) return new URL("file" + PROTOCOL_SEP + urlStr);

			// Encode the url
			URIBuilder ub = new URIBuilder(urlStr);
			return ub.build().toURL();
		} catch (URISyntaxException | MalformedURLException e) {
			throw new RuntimeException("Cannot parse URL " + urlStr, e);
		}
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

	public abstract String getCanonicalPath();

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

	public abstract String getName();

	public abstract String getParent();

	public abstract String getPath();

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
	 * Does this represent a 'file' (not a directory)
	 */
	public abstract boolean isFile();

	public abstract boolean isRemote();

	/**
	 * List of file names under this 'directory'
	 * File names should be returned as canonical paths
	 */
	public abstract ArrayList<String> list();

	//	protected String localPath() {
	//		StringBuilder sb = new StringBuilder();
	//		sb.append(Config.get().getTmpDir());
	//		sb.append("/" + url.getProtocol());
	//
	//		// Authority: Host and port
	//		for (String part : url.getAuthority().split("[:\\.]")) {
	//			if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
	//		}
	//
	//		// Path
	//		for (String part : url.getPath().split("/")) {
	//			if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
	//		}
	//
	//		// Query
	//		for (String part : url.getPath().split("&")) {
	//			if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
	//		}
	//
	//		return sb.toString();
	//	}
	//
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

	public abstract boolean upload(String locaLFileName);

}
