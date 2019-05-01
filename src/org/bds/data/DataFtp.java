package org.bds.data;

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

	protected String hostname;

	public DataFtp(String urlStr) {
		super();
		uri = parseUrl(urlStr);
		hostname = uri.getHost();
		canWrite = false;
	}

	@Override
	public boolean delete() {
		throw new RuntimeException("Unimplemented!");
	}

	@Override
	public void deleteOnExit() {
		throw new RuntimeException("Unimplemented deleteOnExit for FTP data!");
	}

	@Override
	public boolean download(String localFileName) {
		mkdirsLocal(localFileName);
		FtpConnectionFactory.get().download(uri, localFileName);
		return true;
	}

	@Override
	public boolean isDirectory() {
		throw new RuntimeException("Unimplemented!");
	}

	@Override
	public boolean isFile() {
		return true;
	}

	@Override
	public ArrayList<String> list() {
		ArrayList<String> filesStr = new ArrayList<>();
		FTPFile[] files = FtpConnectionFactory.get().list(uri);
		for (FTPFile file : files)
			filesStr.add(file.getName());
		return filesStr;
	}

	/**
	 * Cannot create remote dirs in
	 */
	@Override
	public boolean mkdirs() {
		throw new RuntimeException("Unimplemented!");
	}

	@Override
	protected boolean updateInfo() {
		latestUpdate = new Timer(CACHE_TIMEOUT);
		boolean ok = true;
		FTPFile[] files = FtpConnectionFactory.get().list(uri);
		if (files == null || files.length < 1) {
			// Path does not exists
			size = -1;
			canRead = false;
			exists = false;
			lastModified = new Date(0);
		} else if (files.length == 1) {
			// Single file
			FTPFile file = files[0];

			size = file.getSize();
			canRead = true;
			exists = true;

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

			// Last modified
			long epoch = maxTimeStampMillis / 1000;
			lastModified = new Date(epoch);
		}

		// Show information
		if (debug) Timer.showStdErr("Updated infromation for '" + this + "'"//
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
	public boolean upload(String localFileName) {
		throw new RuntimeException("Unimplemented!");
	}

}
