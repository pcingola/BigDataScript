package org.bds.data;

/**
 * A file / directory on an ftp server
 *
 * Use Apache's FTPClient instead of FtpURLConnection?
 * References: http://commons.apache.org/proper/commons-net/apidocs/org/apache/commons/net/ftp/FTPClient.html
 *
 * @author pcingola
 */
public class DataSftp extends DataFtp {

	public DataSftp(String urlStr) {
		super(urlStr);
	}

}
