package ca.mcgill.mcb.pcingola.bigDataScript.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * A file / directory on a web server
 *
 * @author pcingola
 */
public class DataHttp extends Data {

	private static int BUFFER_SIZE = 102400;
	private static long CACHE_TIMEOUT = 1000;

	public final int HTTP_OK = 200; // Connection OK
	public final int HTTP_REDIR = 302; // The requested resource resides temporarily under a different URI
	public final int HTTP_NOTFOUND = 404; // The requested resource resides temporarily under a different URI

	boolean canRead;
	boolean exists;
	Date lastModified;
	long size;
	Timer latestUpdate;

	public DataHttp(String url) {
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
		return false;
	}

	/**
	 * Connect and cache some data
	 */
	protected URLConnection connect(URL url) {
		try {
			if (verbose) Timer.showStdErr("Connecting to " + url);
			URLConnection connection = url.openConnection();

			// Follow redirect? (only for http connections)
			if (connection instanceof HttpURLConnection) {
				for (boolean followRedirect = true; followRedirect;) {
					HttpURLConnection httpConnection = (HttpURLConnection) connection;
					int code = httpConnection.getResponseCode();

					switch (code) {
					case HTTP_OK:
						// Status OK
						return connection;
					case HTTP_REDIR:
						String newUrl = connection.getHeaderField("Location");
						if (verbose) Timer.showStdErr("Following redirect: " + newUrl);
						url = new URL(newUrl);
						connection = url.openConnection();
						break;

					case HTTP_NOTFOUND:
						canRead = false;
						if (verbose) Timer.showStdErr("File '" + url + "' not found on server.");
						return null;

					default:
						canRead = false;
						if (verbose) Timer.showStdErr("Server error " + code + " for URL '" + url + "'");
						return null;
					}
				}
			}
		} catch (Exception e) {
			Timer.showStdErr("ERROR while connecting to " + this);
			throw new RuntimeException(e);
		}

		return null;
	}

	@Override
	public boolean delete() {
		if (verbose) Timer.showStdErr("Cannot delete file '" + getUrl() + "'");
		return false;
	}

	@Override
	public void deleteOnExit() {
		if (verbose) Timer.showStdErr("Cannot delete file '" + getUrl() + "'");
	}

	@Override
	public boolean download() {
		if (isDownloaded()) return true;
		String localFile = localFile();
		return download(localFile);
	}

	/**
	 * Download a file
	 */
	public boolean download(String localFile) {
		URL url = url();

		try {
			URLConnection connection = connect(url);
			if (connection == null) return false;

			// Copy resource to local file, use remote file if no local file name specified
			InputStream is = url.openStream();

			// Print info about resource
			Date date = new Date(connection.getLastModified());
			if (debug) Timer.showStdErr("Downloading file (type: " + connection.getContentType() + ", modified on: " + date + ")");

			// Open local file
			if (verbose) Timer.showStdErr("Local file name: '" + localFile + "'");

			// Create local directory if it doesn't exists
			File file = new File(localFile);
			if (file != null && file.getParent() != null) {
				File path = new File(file.getParent());
				if (!path.exists()) {
					if (verbose) Timer.showStdErr("Local path '" + path + "' doesn't exist, creating.");
					path.mkdirs();
				}
			}

			FileOutputStream os = null;
			os = new FileOutputStream(localFile);

			// Copy to file
			int count = 0, total = 0, lastShown = 0;
			byte data[] = new byte[BUFFER_SIZE];
			while ((count = is.read(data, 0, BUFFER_SIZE)) != -1) {
				os.write(data, 0, count);
				total += count;

				// Show every MB
				if ((total - lastShown) > (1024 * 1024)) {
					if (verbose) System.err.print(".");
					lastShown = total;
				}
			}
			if (verbose) System.err.println("");

			// Close streams
			is.close();
			os.close();
			if (verbose) Timer.showStdErr("Donwload finished. Total " + total + " bytes.");

			return true;
		} catch (Exception e) {
			Timer.showStdErr("ERROR while connecting to " + getUrl());
			throw new RuntimeException(e);
		}
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

	/**
	 * HTTP has no concept of directory
	 */
	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public boolean isDownloaded() {
		// Did we download
		if (localPath == null) localPath = localFile();
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

	@Override
	public boolean isFile() {
		return true;
	}

	@Override
	public ArrayList<String> list() {
		return new ArrayList<>();
	}

	protected String localFile() {
		StringBuilder sb = new StringBuilder();
		sb.append(Config.get().getTmpDir());
		sb.append("/" + scheme);

		for (String part : path.split("/")) {
			if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
		}

		return sb.toString();
	}

	/**
	 * Cannot create dirs in http
	 */
	@Override
	public boolean mkdirs() {
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

	/**
	 * Connect and update info
	 */
	protected boolean updateInfo() {
		URLConnection connection = connect(url());

		latestUpdate = new Timer(CACHE_TIMEOUT);
		boolean ok;
		if (connection == null) {
			// Cannot connect
			canRead = false;
			exists = false;
			lastModified = new Date(0);
			size = 0;
			ok = false;
		} else {
			// Update data
			size = connection.getContentLengthLong();
			canRead = true;
			lastModified = new Date(connection.getLastModified());
			exists = true;
			ok = true;

		}

		// Show information
		if (verbose) Timer.showStdErr("Updated infromation for '" + getUrl() + "'"//
				+ "\n\tcanRead      : " + canRead //
				+ "\n\texists       : " + exists //
				+ "\n\tlast modified: " + lastModified //
				+ "\n\tsize         : " + size //
		);

		return ok;
	}

	/**
	 * Cannot upload to a web server
	 */
	@Override
	public boolean upload() {
		return false;
	}

}
