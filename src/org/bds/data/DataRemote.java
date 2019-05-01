package org.bds.data;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import org.apache.http.client.utils.URIBuilder;
import org.bds.Config;
import org.bds.util.Gpr;
import org.bds.util.Timer;

/**
 * A file / directory on a remote server
 *
 * @author pcingola
 */
public abstract class DataRemote extends Data {

	public static final String TMP_BDS_DATA = "bds";
	public static final long CACHE_TIMEOUT = 1000; // Timeout in milliseconds

	protected boolean canRead;
	protected boolean canWrite;
	protected boolean exists;
	protected Boolean isDir;
	protected Date lastModified;
	protected long size;
	protected Timer latestUpdate;
	protected URI uri;

	public DataRemote() {
		super();
		size = -1;
		lastModified = new Date(0);
	}

	@Override
	public boolean canExecute() {
		return false;
	}

	@Override
	public boolean canRead() {
		updateInfoIfNeeded();
		return canRead;
	}

	@Override
	public boolean canWrite() {
		updateInfoIfNeeded();
		return canWrite;
	}

	@Override
	public void deleteOnExit() {
		throw new RuntimeException("Unimplemented!");
	}

	@Override
	public boolean download() {
		if (isDownloaded()) return true;
		String localFile = localPath();
		return download(localFile);
	}

	@Override
	public boolean exists() {
		updateInfoIfNeeded();
		return exists;
	}

	@Override
	public String getAbsolutePath() {
		return uri.toString();
	}

	@Override
	public String getCanonicalPath() {
		return getAbsolutePath();
	}

	@Override
	public Date getLastModified() {
		updateInfoIfNeeded();
		return lastModified;
	}

	@Override
	public String getLocalPath() {
		if (localPath == null) localPath = localPath();
		return localPath;
	}

	@Override
	public String getName() {
		File path = new File(uri.getPath());
		return path.getName();
	}

	@Override
	public String getParent() {
		try {
			String path = uri.getPath();
			String paren = (new File(path)).getParent();
			URI uriPaern = new URI(uri.getScheme(), uri.getAuthority(), paren, null, null);
			return uriPaern.toString();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Error parsing URL: " + uri, e);
		}
	}

	@Override
	public String getPath() {
		return uri.getPath();
	}

	public URI getUri() {
		return uri;
	}

	public URL getUrl() {
		try {
			return uri.toURL();
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error parsing URI: '" + uri + "'", e);
		}
	}

	@Override
	public boolean isDirectory() {
		if (isDir == null) {
			String path = getPath();
			isDir = (path == null || path.endsWith("/"));
		}
		return isDir;
	}

	@Override
	public boolean isDownloaded(String localPath) {
		if (debug) Gpr.debug("Comparing local file '" + localPath + "' to remote file '" + getAbsolutePath() + "'");

		// Is there a local file
		File localFile = new File(localPath);
		if (!localFile.exists()) return false;

		// Get remote data
		if (needsUpdateInfo()) {
			if (!updateInfo()) return false; // Cannot update information
		}

		// Has local file a different size than remote file?
		if (size <= 0) return false; // Note: Negative size indicates dynamic content
		if (localFile.length() != size) return false;

		// Is local file older than remote file?
		long lflm = localFile.lastModified();
		long rflm = getLastModified().getTime();
		if (lflm < rflm) return false;

		// OK, we have a local file that looks updated respect to the remote file
		return true;
	}

	@Override
	public boolean isFile() {
		return !isDirectory();
	}

	@Override
	public boolean isRemote() {
		return true;
	}

	@Override
	public boolean isUploaded(String localPath) {
		if (debug) Gpr.debug("Comparing local file '" + localPath + "' to remote file '" + getAbsolutePath() + "'");

		// Is there a local file
		File localFile = new File(localPath);
		if (!localFile.exists()) return false;

		// Get remote data
		if (needsUpdateInfo()) {
			if (!updateInfo()) return false; // Cannot update information
		}

		// Has local file a different size than remote file?
		if (localFile.length() != size) return false;

		// Is local file older than remote file?
		long lflm = localFile.lastModified();
		long rflm = getLastModified().getTime();
		if (lflm > rflm) return false;

		// OK, we have a local file that looks updated respect to the remote file
		return true;
	}

	protected String localPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Config.get().getTmpDir() + "/" + TMP_BDS_DATA);
		sb.append("/" + uri.getScheme());

		// Authority: Host and port
		if (uri.getAuthority() != null) {
			for (String part : uri.getAuthority().split("[:\\.]")) {
				if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
			}
		}

		// Path
		if (uri.getPath() != null) {
			for (String part : uri.getPath().split("/")) {
				if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
			}
		}

		// Query
		if (uri.getQuery() != null) {
			for (String part : uri.getQuery().split("&")) {
				if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
			}
		}

		return sb.toString();
	}

	public boolean mkdirsLocal() {
		return mkdirsLocal(getLocalPath());
	}

	/**
	 * Create local path
	 */
	protected boolean mkdirsLocal(String localFile) {
		File file = new File(localFile);

		// Create local directory if it doesn't exists
		if (file != null && file.getParent() != null) {
			File path = new File(file.getParent());
			if (path.exists()) return true;

			if (verbose) Timer.showStdErr("Local path '" + path + "' doesn't exist, creating.");
			return path.mkdirs();
		}

		return false;
	}

	protected boolean needsUpdateInfo() {
		return latestUpdate == null || latestUpdate.isExpired();
	}

	protected URI parseUrl(String urlStr) {
		try {
			// No protocol: file
			if (urlStr.indexOf(PROTOCOL_SEP) < 0) return new URI("file" + PROTOCOL_SEP + urlStr);

			// Encode the url
			URIBuilder ub = new URIBuilder(urlStr);
			return ub.build();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Cannot parse URL " + urlStr, e);
		}
	}

	@Override
	public long size() {
		updateInfoIfNeeded();
		return size;
	}

	@Override
	public String toString() {
		return getAbsolutePath() + " <=> " + getLocalPath();
	}

	/**
	 * Connect to remote and update info
	 */
	protected abstract boolean updateInfo();

	public void updateInfoIfNeeded() {
		if (needsUpdateInfo()) updateInfo();
	}

	/**
	 * Update last modified in local copy
	 */
	protected void updateLocalFileLastModified() {
		if (lastModified != null) {
			File file = new File(getLocalPath());
			file.setLastModified(lastModified.getTime());
		}
	}

	/**
	 * Cannot upload to a web server
	 */
	@Override
	public boolean upload() {
		return false;
	}
}
