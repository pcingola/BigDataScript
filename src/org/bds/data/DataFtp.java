package org.bds.data;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

//import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.bds.util.Gpr;
import org.bds.util.Timer;

/**
 * A file / directory on an ftp server
 *
 * Use Apache's FTPClient instead of FtpURLConnection?
 * References: http://commons.apache.org/proper/commons-net/apidocs/org/apache/commons/net/ftp/FTPClient.html
 *
 * @author pcingola
 */
public class DataFtp extends DataRemote {

	private static final long serialVersionUID = 7106541163198536282L;

	public static final long CACHE_TIMEOUT_FTP = 30 * 1000; // Timeout in milliseconds

	protected String hostname;

	public DataFtp(String urlStr) {
		super(urlStr, DataType.FTP);
		uri = parseUrl(urlStr);
		hostname = uri.getHost();
		canWrite = false;
	}

	public DataFtp(URI uri) {
		super(uri.toString(), DataType.FTP);
		this.uri = uri;
		hostname = uri.getHost();
		canWrite = false;
	}

	@Override
	public void deleteOnExit() {
		throw new RuntimeException("Unimplemented 'deleteOnExit' operation for FTP data");
	}

	@Override
	public boolean deleteRemote() {
		throw new RuntimeException("Unimplemented 'delete' operation for FTP data");
	}

	@Override
	public boolean download(Data local) {
		mkdirsLocal(local);
		FtpConnectionFactory.get().download(uri, local.getAbsolutePath());
		return true;
	}

	@Override
	public boolean isDirectory() {
		if (isDir == null) updateInfoIfNeeded();
		return (isDir != null) && isDir;
	}

	@Override
	public boolean isFile() {
		if (isDir == null) updateInfoIfNeeded();
		return (isDir != null) && !isDir;
	}

	/**
	 * List of files in FTP server
	 * Note that we always return an absolute path
	 */
	@Override
	public ArrayList<Data> list() {
		ArrayList<Data> fileList = new ArrayList<>();
		FTPFile[] files = FtpConnectionFactory.get().list(uri);

		String baseUrl = this.toString();
		if (!baseUrl.endsWith("/")) baseUrl += '/';

		for (FTPFile file : files) {
			fileList.add(new DataFtp(baseUrl + file.getName()));
		}
		return fileList;
	}

	/**
	 * Cannot create remote dirs in
	 */
	@Override
	public boolean mkdirs() {
		throw new RuntimeException("Unimplemented 'mkdirs' operation for FTP data");
	}

	@Override
	protected boolean updateInfo() {
		latestUpdate = new Timer(CACHE_TIMEOUT_FTP);
		boolean ok = true;
		FTPFile[] files = FtpConnectionFactory.get().list(uri);
		if (files == null || files.length < 1) {
			// Path does not exists
			size = -1;
			canRead = false;
			exists = false;
			lastModified = new Date(0);
			isDir = null;
		} else if (files.length == 1) {
			// Single file
			FTPFile file = files[0];

			size = file.getSize();
			canRead = true;
			exists = true;
			isDir = false;

			// Last modified
			long epoch = file.getTimestamp().getTimeInMillis() / 1000;
			lastModified = new Date(epoch);
		} else {
			// Directory
			Gpr.debug("MULTIPLE FILES!");
			long maxTimeStampMillis = 0;
			for (FTPFile f : files) {
				maxTimeStampMillis = Math.max(maxTimeStampMillis, f.getTimestamp().getTimeInMillis());
			}

			size = -1;
			canRead = true;
			exists = true;
			isDir = true;

			// Last modified
			long epoch = maxTimeStampMillis / 1000;
			lastModified = new Date(epoch);
		}

		// Show information
		debug("Updated infromation for '" + this + "'"//
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
	public boolean upload(Data local) {
		throw new RuntimeException("Unimplemented 'upload' operation for FTP data");
	}

}
