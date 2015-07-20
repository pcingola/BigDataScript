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

	public static final long CACHE_TIMEOUT = 1000;

	boolean canRead, canWrite;
	boolean exists;
	Date lastModified;
	long size;
	Timer latestUpdate;

	public DataRemote(String url) {
		super(url);
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
	public Date getLastModified() {
		if (needsUpdateInfo()) updateInfo();
		return lastModified;
	}

	@Override
	public String getParent() {
		int idx = path.lastIndexOf('/');
		if (idx > 0) return path.substring(0, idx);
		return null;
	}

	@Override
	public boolean isDownloaded() {
		// Did we download
		if (localPath == null) localPath = localPath();
		if (debug) Gpr.debug("Comparing local file '" + localPath + "' to remote file '" + getUrl() + "'");

		// Is there a local file
		File localFile = new File(localPath);
		if (!localFile.exists()) return false;

		// Get remote data
		if (needsUpdateInfo()) {
			if (!updateInfo()) return false;
		}

		// Has local file a different size than remote file?
		if (localFile.length() != size) return false;

		// Is local file older than remote file?
		long lflm = localFile.lastModified();
		long rflm = lastModified.getTime();
		if (lflm < rflm) return false;

		// OK, we have a local file that looks updated respect to the remote file
		return true;
	}

	protected boolean needsUpdateInfo() {
		return latestUpdate == null || latestUpdate.isExpired();
	}

	@Override
	public long size() {
		if (needsUpdateInfo()) updateInfo();
		return size;
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
