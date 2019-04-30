package org.bds.data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.net.ftp.FTPClient;
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

	/**
	 * Close connection
	 */
	protected void close() {
		FtpConnectionFactory.get().close(uri);
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
		FTPClient ftp = open();
		OutputStream output = null;
		try {
			mkdirsLocal(localFileName);
			output = new FileOutputStream(localFileName);
		} catch (FileNotFoundException e1) {
			String msg = "Error opening local file '" + localFileName + "'";
			Timer.showStdErr(msg);
			throw new RuntimeException(msg, e1);
		}

		String remotePath = getPath();
		try {
			ftp.retrieveFile(remotePath, output);
		} catch (IOException e) {
			String msg = "Error downloading file '" + remotePath + "' from host '" + hostname + "'";
			Timer.showStdErr(msg);
			throw new RuntimeException(msg, e);
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				String msg = "Error closing file '" + localFileName + "'";
				Timer.showStdErr(msg);
				throw new RuntimeException(msg, e);
			}
		}
		close();
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
		FTPClient ftp = open();
		ArrayList<String> filesStr = new ArrayList<>();
		try {
			FTPFile[] files = ftp.listFiles(getPath());
			for (FTPFile file : files)
				filesStr.add(file.getName());
		} catch (IOException e) {
			String msg = "Error reading remote directory '" + getPath() + "', from FTP server '" + hostname + "'";
			Timer.showStdErr(msg);
			throw new RuntimeException(msg, e);
		}
		close();
		return filesStr;
	}

	/**
	 * Cannot create remote dirs in
	 */
	@Override
	public boolean mkdirs() {
		throw new RuntimeException("Unimplemented!");
	}

	protected FTPClient open() {
		return FtpConnectionFactory.get().open(uri);
	}

	@Override
	protected boolean updateInfo() {
		latestUpdate = new Timer(CACHE_TIMEOUT);
		boolean ok = true;
		FTPClient ftp = open();
		String path = getPath();
		Gpr.debug("path: " + path);
		try {
			FTPFile[] files = ftp.listFiles(path);
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
		} catch (IOException e) {
			String msg = "Error reading remote path '" + getPath() + "', from FTP server '" + hostname + "'";
			Timer.showStdErr(msg);
			throw new RuntimeException(msg, e);
		}

		//		if (connection == null) {
		//			// Cannot connect
		//			canRead = false;
		//			exists = false;
		//			lastModified = new Date(0);
		//			size = 0;
		//			ok = false;
		//		} else {
		//			// Update data
		//			size = ftp..getContentLengthLong(); // Could be negative (unspecified)
		//			canRead = true;
		//			exists = true;
		//
		//			// Last modified
		//			long lastMod = connection.getLastModified();
		//			if (lastMod == 0) lastMod = connection.getDate(); // If last_modified is not found, use 'date' (e.g. dynamic content)
		//			lastModified = new Date(lastMod);
		//
		//			ok = true;
		//
		//		}
		close();

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
