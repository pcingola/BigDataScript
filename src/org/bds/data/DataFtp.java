package org.bds.data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
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

	protected FTPClient ftp;
	protected String hostname, username, password;

	public DataFtp(String urlStr) {
		super();
		url = parseUrl(urlStr);
		hostname = url.getHost();
		canWrite = false;
	}

	/**
	 * Close connection
	 */
	protected void close() {
		try {
			ftp.disconnect();
		} catch (IOException e) {
			Timer.showStdErr("ERROR while disconnecting from '" + hostname + "'");
		}
	}

	/**
	 * Connect to remote server
	 */
	protected void connect() {
		if (ftp != null && ftp.isConnected()) return;
		ftp = newClient();

		// Configure
		FTPClientConfig config = new FTPClientConfig();
		config.setLenientFutureDates(true);
		ftp.configure(config);

		// Connect
		try {
			if (verbose) Timer.showStdErr("Connecting to '" + hostname + "'");
			ftp.connect(hostname);
			// After connection attempt, you should check the reply code to verify success.
			int reply = ftp.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				String msg = "FTP Connection error, host '" + hostname + "', URL: '" + url + "', reply code '" + reply + "'";
				Timer.showStdErr(msg);
				throw new RuntimeException(msg);
			}
		} catch (Exception e) {
			String msg = "ERROR while connecting to '" + hostname + "'";
			Timer.showStdErr(msg);
			throw new RuntimeException(msg, e);
		}

		// Log into server
		setUserInfo();
		try {
			ftp.login(username, password);
		} catch (IOException e) {
			String msg = "ERROR while logging into server '" + hostname + "'";
			Timer.showStdErr(msg);
			throw new RuntimeException(msg, e);
		}

		// Use binary mode
		ftp.setControlKeepAliveTimeout(60);
		try {
			ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);
		} catch (IOException e) {
			String msg = "Unable to set FTP transfer to binary mode for host '" + hostname + "', URL: '" + url + "'";
			Timer.showStdErr(msg);
			throw new RuntimeException(msg, e);
		}
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
		connect();
		OutputStream output = null;
		try {
			mkdirsLocal(localFileName);
			output = new FileOutputStream(localFileName);
		} catch (FileNotFoundException e1) {
			throw new RuntimeException("Error opening local file '" + localFileName + "'");
		}

		String remotePath = getPath();
		try {
			Gpr.debug("Remote path '" + remotePath + "'");
			ftp.retrieveFile(remotePath, output);
		} catch (IOException e) {
			throw new RuntimeException("Error downloading file '" + remotePath + "' from host '" + hostname + "'", e);
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				throw new RuntimeException("Error closing file '" + localFileName + "'", e);
			}
		}
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
		throw new RuntimeException("Unimplemented!");
	}

	/**
	 * Cannot create remote dirs in
	 */
	@Override
	public boolean mkdirs() {
		throw new RuntimeException("Unimplemented!");
	}

	protected FTPClient newClient() {
		return new FTPClient();
	}

	/**
	 * Parse user info from URL
	 */
	void setUserInfo() {
		String userInfo = url.getUserInfo();
		if (userInfo == null) {
			username = "anonymous";
			password = "anonymous@dev.null";
		} else {
			String[] fields = userInfo.split(":", 2);
			username = fields[0];
			password = (fields.length > 1 ? fields[1] : "");
		}
	}

	@Override
	protected boolean updateInfo() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Cannot upload to a web server
	 */
	@Override
	public boolean upload(String localFileName) {
		throw new RuntimeException("Unimplemented!");
	}

}
