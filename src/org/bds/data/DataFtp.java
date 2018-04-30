package org.bds.data;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.bds.util.Timer;

import sun.net.www.protocol.ftp.FtpURLConnection;

/**
 * A file / directory on an ftp server
 *
 * Use Apache's FTPClient instead of FtpURLConnection?
 * References: http://commons.apache.org/proper/commons-net/apidocs/org/apache/commons/net/ftp/FTPClient.html
 *
 * @author pcingola
 */
public class DataFtp extends DataHttp {

	public DataFtp(String urlStr) {
		super(urlStr);
	}

	@Override
	protected void close(URLConnection connection) {
		((FtpURLConnection) connection).close();
	}

	/**
	 * Connect and cache some data
	 */
	@Override
	protected URLConnection connect() {
		try {
			if (verbose) Timer.showStdErr("Connecting to " + url);
			return url.openConnection();
		} catch (Exception e) {
			Timer.showStdErr("ERROR while connecting to " + this);
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean delete() {
		throw new RuntimeException("Unimplemented!");
	}

	@Override
	public void deleteOnExit() {
		throw new RuntimeException("Unimplemented!");
	}

	@Override
	public String getPath() {
		return url.getPath();
	}

	@Override
	public URL getUrl() {
		return url;
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
	 * Cannot create remote dirs in http
	 */
	@Override
	public boolean mkdirs() {
		throw new RuntimeException("Unimplemented!");
	}

	/**
	 * Cannot upload to a web server
	 */
	@Override
	public boolean upload(String localFileName) {
		throw new RuntimeException("Unimplemented!");
	}

}
