package ca.mcgill.mcb.pcingola.bigDataScript.data;

import java.io.File;
import java.util.Date;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * A file / directory on a remote server
 *
 * @author pcingola
 */
public abstract class DataRemote extends Data {

	public static final String TMP_BDS_DATA = "bds";
	public static final long CACHE_TIMEOUT = 1000; // Timeout in milliseconds

	protected boolean canRead, canWrite;
	protected boolean exists;
	protected Date lastModified;
	protected long size;
	protected Timer latestUpdate;

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
		return getAbsolutePath();
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

	protected abstract String localPath();

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

	@Override
	public long size() {
		if (needsUpdateInfo()) updateInfo();
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
