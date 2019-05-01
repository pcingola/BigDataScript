package org.bds.data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.bds.Config;
import org.bds.util.Gpr;
import org.bds.util.Timer;
import org.bds.util.Tuple;

/**
 * Provides FTP connections: This is used in order to have a single connection
 * per host (instead of opening one connection for each remote object)
 *
 * @author pcingola
 *
 */
public class FtpConnectionFactory extends TimerTask {

	public static boolean verbose = true;
	public static boolean debug = false;

	public static int TIMER_DELAY = 1 * 1000;
	public static int TIMER_PERIOD = 1 * 1000;
	public static int MAX_CONNECTION_TIME = 10 * 1000;

	private static FtpConnectionFactory instance = null;
	private static java.util.Timer timer = null;

	Map<String, FTPClient> ftpClientByKey;
	Map<String, Long> latestUsage;
	boolean running;

	/**
	 * Get factory instance
	 */
	public static FtpConnectionFactory get() {
		if (instance == null) {
			debug = Config.get().isDebug();
			debug = true;
			if (debug) Gpr.debug("Creating new FtpConnectionFactory instance");
			instance = new FtpConnectionFactory();
			// Initialize a timer thread to periodically review connections
			timer = new java.util.Timer(instance.getClass().getName());
			timer.schedule(instance, TIMER_DELAY, TIMER_PERIOD);
		}
		return instance;
	}

	/**
	 * Kill instance
	 */
	public static void kill() {
		if (debug) Gpr.debug("Killing FtpConnectionFactory timer");
		if (instance != null) instance.running = false;
		if (timer != null) timer.cancel();
		timer = null;
		instance = null;
	}

	private FtpConnectionFactory() {
		ftpClientByKey = new HashMap<>();
		latestUsage = new HashMap<>();
		running = true;
	}

	/**
	 * Close a connection
	 */
	public void close(String key) {
		if (debug) Gpr.debug("FtpConnectionFactory: Closing connection '" + key + "'");
		FTPClient ftp = delete(key);
		if (ftp != null) disconnect(ftp, key);
	}

	/**
	 * Connect to remote server
	 */
	private void connect(FTPClient ftp, URI uri, String key) {
		synchronized (ftp) {
			if (ftp.isConnected()) return;
			if (debug) Gpr.debug("FtpConnectionFactory: Connecting to '" + key + "'");

			// Configure
			FTPClientConfig config = new FTPClientConfig();
			config.setLenientFutureDates(true);
			ftp.configure(config);

			// Connect
			try {
				if (verbose) Timer.showStdErr("Connecting to '" + key + "'");
				ftp.connect(uri.getHost());
				// After connection attempt, you should check the reply code to verify success.
				int reply = ftp.getReplyCode();

				if (!FTPReply.isPositiveCompletion(reply)) {
					ftp.disconnect();
					String msg = "FTP Connection error, host '" + key + "', reply code '" + reply + "'";
					Timer.showStdErr(msg);
					throw new RuntimeException(msg);
				}
			} catch (Exception e) {
				String msg = "ERROR while connecting to '" + key + "'";
				Timer.showStdErr(msg);
				throw new RuntimeException(msg, e);
			}

			// Log into server
			Tuple<String, String> userPass = setUserInfo(uri);
			try {
				ftp.login(userPass.first, userPass.second);
			} catch (IOException e) {
				String msg = "ERROR while logging into server '" + key + "'";
				Timer.showStdErr(msg);
				throw new RuntimeException(msg, e);
			}

			// Use binary mode
			ftp.setControlKeepAliveTimeout(60);
			try {
				ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			} catch (IOException e) {
				String msg = "Unable to set FTP transfer to binary mode for host '" + key + "'";
				Timer.showStdErr(msg);
				throw new RuntimeException(msg, e);
			}
		}
		if (debug) Gpr.debug("FtpConnectionFactory: Connected to '" + key + "'");
	}

	/**
	 * Delete a key
	 * @return Deleted FTPClient
	 */
	private synchronized FTPClient delete(String key) {
		if (debug) Gpr.debug("FtpConnectionFactory: deleting key '" + key + "'");
		FTPClient ftp = ftpClientByKey.remove(key);
		latestUsage.remove(key);
		return ftp;
	}

	/**
	 * Disconnect and FTP client
	 */
	private void disconnect(FTPClient ftp, String key) {
		if (ftp == null) return;
		synchronized (ftp) {
			if (!ftp.isConnected()) return;
			try {
				ftp.disconnect();
			} catch (IOException e) {
				Gpr.debug("ERROR while disconnecting from '" + key + "'");
			}
		}
	}

	/**
	 * Download a file
	 */
	public boolean download(URI uri, String localFileName) {
		FTPClient ftp = open(uri);
		return retrieveFile(ftp, uri, localFileName);
	}

	/**
	 * Time elapsed since latest usage
	 */
	private synchronized long elapsed(String key) {
		long now = (new Date()).getTime();
		long latest = latestUsage.getOrDefault(key, 0L);
		return now - latest;
	}

	/**
	 * Get or create a new FTP client
	 */
	private synchronized FTPClient getOrCreateFtpClient(String key) {
		FTPClient ftp = ftpClientByKey.get(key);
		if (ftp == null) {
			if (debug) Gpr.debug("FtpConnectionFactory: Creating FTP client for key '" + key + "'");
			ftp = new FTPClient();
			ftpClientByKey.put(key, ftp);
		}
		return ftp;
	}

	/**
	 * Convert a URI to a key.
	 * Note, we only need host information in the key.
	 * The path to the path is ignored
	 */
	private String key(URI uri) {
		int port = uri.getPort();
		String userInfo = uri.getUserInfo();
		return "ftp://" + (userInfo != null ? userInfo + '@' : "") + uri.getHost() + (port > 0 ? ':' + port : "");
	}

	/**
	 * List files from URI
	 */
	public FTPFile[] list(URI uri) {
		FTPClient ftp = open(uri);
		return listFiles(ftp, uri);
	}

	/**
	 * List files using an FTP client
	 */
	public FTPFile[] listFiles(FTPClient ftp, URI uri) {
		synchronized (ftp) {
			try {
				return ftp.listFiles(uri.getPath());
			} catch (IOException e) {
				String msg = "Error reading remote directory '" + uri + "'";
				Timer.showStdErr(msg);
				throw new RuntimeException(msg, e);
			}
		}
	}

	/**
	 * Create a new client (open a connection)
	 * @return
	 */
	private FTPClient open(URI uri) {
		String key = key(uri);
		if (debug) Gpr.debug("FtpConnectionFactory: Open FTP client '" + key + "'");
		FTPClient ftp = getOrCreateFtpClient(key);
		connect(ftp, uri, key);
		update(key);
		return ftp;
	}

	/**
	 * Retrieve a remote file using FTPClient
	 */
	public boolean retrieveFile(FTPClient ftp, URI uri, String localFileName) {
		synchronized (ftp) {
			OutputStream output = null;
			try {
				output = new FileOutputStream(localFileName);
			} catch (FileNotFoundException e1) {
				String msg = "Error opening local file '" + localFileName + "'";
				Timer.showStdErr(msg);
				return false;
			}

			String remotePath = uri.getPath();
			try {
				ftp.retrieveFile(remotePath, output);
			} catch (IOException e) {
				String msg = "Error downloading file  from '" + uri + "'";
				Timer.showStdErr(msg);
				return false;
			} finally {
				try {
					output.close();
				} catch (IOException e) {
					String msg = "Error closing file '" + localFileName + "'";
					Timer.showStdErr(msg);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Timer action: Review and cleanup ftp connections
	 */
	@Override
	public void run() {
		if (debug) Gpr.debug("FtpConnectionFactory cleanup");

		for (String key : ftpClientByKey.keySet()) {
			if (!running) {
				if (debug) Gpr.debug("FtpConnectionFactory killed: Abort cleanup loop (run)");
				return;
			}

			// Has the connection expired?
			if (elapsed(key) > MAX_CONNECTION_TIME) close(key);
		}

		// If there are no further connections, this thread can be killed
		if (ftpClientByKey.isEmpty()) {
			if (debug) Gpr.debug("FtpConnectionFactory: No more ftp clients, killing timer task");
			kill();
		}
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

	/**
	 * Increment usage count
	 */
	private synchronized void update(String key) {
		long epoch = (new Date()).getTime();
		latestUsage.put(key, epoch);
	}

}
