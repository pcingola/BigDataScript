package org.bds.data;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.apache.http.client.utils.URIBuilder;
import org.bds.Config;
import org.bds.run.BdsThread;
import org.bds.run.BdsThreads;
import org.bds.util.Gpr;
import org.bds.util.Timer;

/**
 * A file / directory on a remote server
 *
 * @author pcingola
 */
public abstract class DataRemote extends Data {

	private static final long serialVersionUID = 2140929556406130349L;

	public static final String TMP_BDS_DATA = "bds";
	public static final long CACHE_TIMEOUT = 10 * 1000; // Timeout in milliseconds

	protected boolean canRead;
	protected boolean canWrite;
	protected boolean exists;
	protected Boolean isDir;
	protected Date lastModified;
	protected long size;
	protected Timer latestUpdate;
	protected URI uri;

	public DataRemote(String uri, DataType fileType) {
		super(uri, fileType);
		size = -1;
		lastModified = new Date(0);
		relative = false;
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
	protected String createUrl() {
		if (relative) return urlOri;
		return uri.toString();
	}

	/**
	 * Delete both remote and local files
	 */
	@Override
	public boolean delete() {
		if (!isFile()) return false;
		boolean ok = deleteLocal();
		ok = deleteRemote() || ok;
		resetInfo();
		return ok;
	}

	/**
	 * Delete local file, return true on success
	 */
	public boolean deleteLocal() {
		String localFile = getLocalPath();
		debug("Deleting local file '" + localFile + "', cached from remote file '" + this + "'");
		resetInfo();
		File f = new File(localFile);
		return f.delete();
	}

	@Override
	public void deleteOnExit() {
		BdsThread bdsThread = BdsThreads.getInstance().getOrRoot();
		if (bdsThread != null) {
			debug("Deleting on exit '" + this + "'");
			bdsThread.rmOnExit(this);
		}
		throw new RuntimeException("Could not get Bds thread!");
	}

	public abstract boolean deleteRemote();

	@Override
	public boolean download() {
		if (isDownloaded()) return true;
		String localFile = localPath();
		return download(factory(localFile));
	}

	@Override
	public boolean exists() {
		updateInfoIfNeeded();
		return exists;
	}

	@Override
	public String getAbsolutePath() {
		return getPath();
	}

	/**
	 * There is no way to know the canonical path on a remote file system (http, ftp, etc.)
	 */
	@Override
	public String getCanonicalPath() {
		return getPath();
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
	public Data getParent() {
		String path = uri.getPath();
		String paren = (new File(path)).getParent();
		URI uriPaern = replacePath(paren);
		return factory(uriPaern, null);
	}

	@Override
	public String getPath() {
		return uri.getPath();
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
	public boolean isDownloaded(Data local) {
		debug("Comparing local file '" + local + "' to remote file '" + this + "'");

		// Is there a local file
		if (!local.exists()) return false;

		// Get remote data
		if (needsUpdateInfo()) {
			if (!updateInfo()) return false; // Cannot update information
		}

		// Has local file a different size than remote file?
		if (size <= 0) return false; // Note: Negative size indicates dynamic content
		if (local.size() != size) return false;

		// Is local file older than remote file? Then we have a local
		// file that looks updated respect to the remote file
		return getLastModified().getTime() <= local.getLastModified().getTime();
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
	public boolean isUploaded(Data local) {
		debug("Comparing local file '" + local + "' to remote file '" + this + "'");

		// Is there a local file
		if (!local.exists()) return false;

		// Get remote data
		if (needsUpdateInfo()) {
			if (!updateInfo()) return false; // Cannot update information
		}

		// Has local file a different size than remote file?
		if (local.size() != size) return false;

		// Is local file newer than remote file? Then we have a
		// remote file that looks updated respect to the local file
		return local.getLastModified().getTime() <= getLastModified().getTime();
	}

	/**
	 * Join a segment to this path
	 */
	@Override
	public Data join(Data segment) {
		File fpath = new File(getPath());
		File fjoin = new File(fpath, segment.getPath());
		URI uri = replacePath(fjoin.getAbsolutePath());
		return factory(uri, null);
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
		return mkdirsLocal(factory(getLocalPath()));
	}

	/**
	 * Create local path
	 */
	protected boolean mkdirsLocal(Data local) {
		if (local == null) return false;

		Data paren = local.getParent();
		if (paren == null) return false;

		if (paren.exists()) return true;

		log("Local path '" + paren + "' doesn't exist, creating.");
		return paren.mkdirs();
	}

	protected boolean needsUpdateInfo() {
		return latestUpdate == null || latestUpdate.isExpired();
	}

	protected URI parseUrl(String urlStr) {
		try {
			// No protocol: file
			if (urlStr.indexOf(PROTOCOL_SEP) < 0) return new URI("file" + PROTOCOL_SEP + urlStr);

			// Encode the url
			return new URI(urlStr);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Cannot parse URL " + urlStr, e);
		}
	}

	protected URI replacePath(String absPath) {
		URIBuilder ub = new URIBuilder();
		ub.setScheme(uri.getScheme());
		ub.setUserInfo(uri.getUserInfo());
		ub.setHost(uri.getHost());
		ub.setPort(uri.getPort());
		ub.setPath(absPath);
		try {
			return ub.build();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Error building URI from: '" + uri + "' and '" + absPath + "'");
		}
	}

	protected void resetInfo() {
		latestUpdate = null;
	}

	@Override
	public long size() {
		updateInfoIfNeeded();
		return size;
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

}
