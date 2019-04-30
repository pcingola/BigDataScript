package org.bds.data;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.bds.util.Timer;
import org.bds.util.Tuple;

/**
 * Provides FTP connections: This is used in order to have a single connection
 * per host (instead of opening one connection for each remote object)
 *
 * @author pcingola
 *
 */
public class FtpConnectionFactory {

	public static boolean verbose = true;
	private static FtpConnectionFactory instance = null;

	Map<String, FTPClient> ftpClientByHostName;
	Map<String, Integer> usageCount;

	public static FtpConnectionFactory get() {
		if (instance == null) instance = new FtpConnectionFactory();
		return instance;
	}

	private FtpConnectionFactory() {
		ftpClientByHostName = new HashMap<>();
		usageCount = new HashMap<>();
	}

	/**
	 * Close a connection
	 */
	public void close(URI uri) {
		String key = key(uri);
		int count = dec(key);
		if (count <= 0) {
			FTPClient ftp = get(key);
			try {
				ftp.disconnect();
			} catch (IOException e) {
				Timer.showStdErr("ERROR while disconnecting from '" + uri.getHost() + "'");
			}
		}
	}

	/**
	 * Connect to remote server
	 */
	private void connect(FTPClient ftp, URI uri) {
		if (ftp != null && ftp.isConnected()) return;

		String hostname = uri.getHost();

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
				String msg = "FTP Connection error, host '" + hostname + "', URL: '" + uri + "', reply code '" + reply + "'";
				Timer.showStdErr(msg);
				throw new RuntimeException(msg);
			}
		} catch (Exception e) {
			String msg = "ERROR while connecting to '" + hostname + "'";
			Timer.showStdErr(msg);
			throw new RuntimeException(msg, e);
		}

		// Log into server
		Tuple<String, String> userPass = setUserInfo(uri);
		try {
			ftp.login(userPass.first, userPass.second);
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
			String msg = "Unable to set FTP transfer to binary mode for host '" + hostname + "', URL: '" + uri + "'";
			Timer.showStdErr(msg);
			throw new RuntimeException(msg, e);
		}
	}

	/**
	 * Decrement usage count, remove counter when non-positive
	 */
	private synchronized int dec(String key) {
		int count = usageCount.getOrDefault(key, 0) - 1;

		if (count <= 0) usageCount.remove(key);
		else usageCount.put(key, count);

		return count;
	}

	private FTPClient get(String key) {
		return ftpClientByHostName.get(key);
	}

	/**
	 * Increment usage count
	 */
	private synchronized void inc(String key) {
		int count = usageCount.getOrDefault(key, 0);
		usageCount.put(key, count + 1);
	}

	/**
	 * Convert a URI to a key.
	 * Note, we only need host information in the key. The path to the path is ignored
	 */
	private String key(URI uri) {
		return uri.getHost() + '\t' + uri.getPort() + '\t' + uri.getUserInfo();
	}

	/**
	 * Create a new client (open a connection)
	 * @return
	 */
	public FTPClient open(URI uri) {
		String key = key(uri);
		FTPClient ftp = get(key);
		if (ftp == null) {
			ftp = new FTPClient();
			ftpClientByHostName.put(key, ftp);
			connect(ftp, uri);
		}
		inc(key);
		return ftp;
	}

	/**
	 * Parse user info from URL
	 */
	private Tuple<String, String> setUserInfo(URI uri) {
		String userInfo = uri.getUserInfo();
		String username, password;
		if (userInfo == null) {
			username = "anonymous";
			password = "anonymous@dev.null";
		} else {
			String[] fields = userInfo.split(":", 2);
			username = fields[0];
			password = (fields.length > 1 ? fields[1] : "");
		}
		return new Tuple<>(username, password);
	}

}
