package org.bds.data;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import org.bds.util.Timer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A file / directory on a web server
 *
 * @author pcingola
 */
public class DataHttp extends DataRemote {

	private static final int BUFFER_SIZE = 100 * 1024;
	public static final int HTTP_OK = 200; // Connection OK
	public static final int HTTP_MOVED_PERMANENTLY = 301; // The requested resource moved permanently to a different URI
	public static final int HTTP_REDIR = 302; // The requested resource resides temporarily under a different URI
	public static final int HTTP_NOTFOUND = 404; // The requested resource resides temporarily under a different URI

	URLConnection connection;

	public DataHttp(String urlStr) {
		super(urlStr);
		uri = parseUrl(urlStr);
		canWrite = false;
	}

	public DataHttp(URI uri) {
		super(uri.toString());
		this.uri = uri;
		canWrite = false;
	}

	/**
	 * Close connection
	 */
	protected void close() {
		// Nothing to do
	}

	/**
	 * Connect and cache some data
	 */
	protected URLConnection connect() {
		try {
			if (verbose) Timer.showStdErr("Connecting to " + uri);
			URL url = uri.toURL();
			connection = url.openConnection();

			// Follow redirect? (only for http connections)
			if (connection instanceof HttpURLConnection) {
				for (boolean followRedirect = true; followRedirect;) {
					HttpURLConnection httpConnection = (HttpURLConnection) connection;
					int code = httpConnection.getResponseCode();

					switch (code) {
					case HTTP_OK:
						// Status OK
						return connection;

					case HTTP_MOVED_PERMANENTLY:
					case HTTP_REDIR:
						String newUrl = connection.getHeaderField("Location");
						if (verbose) Timer.showStdErr("Following redirect: " + newUrl);
						url = new URL(newUrl);
						connection = url.openConnection();
						break;

					case HTTP_NOTFOUND:
						canRead = false;
						if (verbose) Timer.showStdErr("File '" + uri + "' not found on server.");
						return null;

					default:
						canRead = false;
						if (verbose) Timer.showStdErr("Server error " + code + " for URL '" + uri + "'");
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
		if (verbose) Timer.showStdErr("Cannot delete file '" + this + "'");
		return false;
	}

	/**
	 * Download a file
	 */
	@Override
	public boolean download(Data local) {
		URLConnection connection = null;
		try {
			// Connect and update info
			connection = connect();
			if (connection == null) return false;
			updateInfo(connection);

			// Copy resource to local file, use remote file if no local file name specified
			InputStream is = uri.toURL().openStream();

			// Open local file
			if (verbose) Timer.showStdErr("Local file name: '" + local + "'");

			// Create local directory if it doesn't exists
			mkdirsLocal(local);
			FileOutputStream os = new FileOutputStream(local.getAbsolutePath());

			// Copy to file
			int count = 0, total = 0, lastShown = 0;
			byte data[] = new byte[BUFFER_SIZE];
			while ((count = is.read(data, 0, BUFFER_SIZE)) != -1) {
				os.write(data, 0, count);
				total += count;

				if (verbose) {
					// Show every MB
					if ((total - lastShown) > (1024 * 1024)) {
						System.err.print(".");
						lastShown = total;
					}
				}
			}
			if (verbose) System.err.println("");

			// Close streams
			is.close();
			os.close();
			if (verbose) Timer.showStdErr("Donwload finished. Total " + total + " bytes.");

			// Update file's last modified
			updateLocalFileLastModified();

			return true;
		} catch (Exception e) {
			Timer.showStdErr("ERROR while connecting to " + this);
			throw new RuntimeException(e);
		} finally {
			close();
		}
	}

	@Override
	public ArrayList<Data> list() {
		// Download a page and extract all 'hrefs'
		ArrayList<Data> fileList = new ArrayList<>();
		try {
			// Read HTML page
			String baseUrl = uri.toURL().toString();
			Document doc = Jsoup.connect(baseUrl).get();

			// Parse html, add all 'href' links
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				String href = link.attr("abs:href");
				fileList.add(new DataHttp(href));
			}
		} catch (Exception e) {
			if (verbose) Timer.showStdErr("ERROR while listing file from '" + this + "'");
		} finally {
			close();
		}
		return fileList;
	}

	/**
	 * Cannot create remote dirs in http
	 */
	@Override
	public boolean mkdirs() {
		return false;
	}

	/**
	 * Connect and update info
	 */
	@Override
	protected boolean updateInfo() {
		URLConnection connection = connect();
		boolean ok = updateInfo(connection);
		close();
		return ok;
	}

	protected boolean updateInfo(URLConnection connection) {
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
			size = connection.getContentLengthLong(); // Could be negative (unspecified)
			canRead = true;
			exists = true;

			// Last modified
			long lastMod = connection.getLastModified();
			if (lastMod == 0) lastMod = connection.getDate(); // If last_modified is not found, use 'date' (e.g. dynamic content)
			lastModified = new Date(lastMod);

			ok = true;

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
	public boolean upload(Data local) {
		return false;
	}

}
