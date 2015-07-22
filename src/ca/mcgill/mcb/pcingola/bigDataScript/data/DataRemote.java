package ca.mcgill.mcb.pcingola.bigDataScript.data;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * A file / directory on a remote server
 *
 * @author pcingola
 */
public abstract class DataRemote extends Data {

	public static final long CACHE_TIMEOUT = 1000;

	protected URL url;
	protected boolean canRead, canWrite;
	protected boolean exists;
	protected Date lastModified;
	protected long size;
	protected Timer latestUpdate;

	public DataRemote(URL url) {
		super();
		this.url = url;
	}

	@Override
	public boolean canExecute() {
		return false;
	}

	@Override
	public boolean canRead() {
		if (needsUpdateInfo()) updateInfo();
		return canRead;
	}

	@Override
	public boolean canWrite() {
		if (needsUpdateInfo()) updateInfo();
		return canWrite;
	}

	@Override
	public boolean download() {
		if (isDownloaded()) return true;
		String localFile = localPath();
		return download(localFile);
	}

	@Override
	public boolean exists() {
		if (needsUpdateInfo()) updateInfo();
		return exists;
	}

	@Override
	public String getCanonicalPath() {
		return url.toString();
	}

	@Override
	public Date getLastModified() {
		if (needsUpdateInfo()) updateInfo();
		return lastModified;
	}

	@Override
	public String getLocalPath() {
		if (localPath == null) localPath = localPath();
		return localPath;
	}

	@Override
	public String getName() {
		File path = new File(url.getPath());
		return path.getName();
	}

	@Override
	public String getParent() {
		try {
			String path = url.getPath();
			String paren = (new File(path)).getParent();
			URI uri = new URI(url.getProtocol(), url.getAuthority(), paren, null, null);
			return uri.toString();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Error parsing URL: " + url, e);
		}
	}

	@Override
	public String getPath() {
		return url.getPath();
	}

	public URL getUrl() {
		return url;
	}

	@Override
	public boolean isDownloaded(String localPath) {
		if (debug) Gpr.debug("Comparing local file '" + localPath + "' to remote file '" + getUrl() + "'");

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
		if (lflm < rflm) return false;

		// OK, we have a local file that looks updated respect to the remote file
		return true;
	}

	@Override
	public boolean isRemote() {
		return true;
	}

	@Override
	public boolean isUploaded(String localPath) {
		if (debug) Gpr.debug("Comparing local file '" + localPath + "' to remote file '" + getUrl() + "'");

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
		sb.append(Config.get().getTmpDir());
		sb.append("/" + url.getProtocol());

		// Authority: Host and port
		if (url.getAuthority() != null) {
			for (String part : url.getAuthority().split("[:\\.]")) {
				if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
			}
		}

		// Path
		if (url.getPath() != null) {
			for (String part : url.getPath().split("/")) {
				if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
			}
		}

		// Query
		if (url.getQuery() != null) {
			for (String part : url.getQuery().split("&")) {
				if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
			}
		}

		return sb.toString();
	}

	protected boolean needsUpdateInfo() {
		return latestUpdate == null || latestUpdate.isExpired();
	}

	@Override
	public long size() {
		if (needsUpdateInfo()) updateInfo();
		return size;
	}

	@Override
	public String toString() {
		return url + " <=> " + getLocalPath();
	}

	/**
	 * Connect to remote and update info
	 */
	protected abstract boolean updateInfo();

	/**
	 * Cannot upload to a web server
	 */
	@Override
	public boolean upload() {
		return false;
	}
}
