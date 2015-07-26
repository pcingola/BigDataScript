package ca.mcgill.mcb.pcingola.bigDataScript.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.utils.URIBuilder;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * A file / directory on a web server
 *
 * @author pcingola
 */
public class DataHttp extends DataRemote {

	private static int BUFFER_SIZE = 100 * 1024;

	public final int HTTP_OK = 200; // Connection OK
	public final int HTTP_REDIR = 302; // The requested resource resides temporarily under a different URI
	public final int HTTP_NOTFOUND = 404; // The requested resource resides temporarily under a different URI

	protected URL url;

	public DataHttp(String urlStr) {
		super();
		url = parseUrl(urlStr);
		canWrite = false;
	}

	/**
	 * Connect and cache some data
	 */
	protected URLConnection connect() {
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

	/**
	 * Download a file
	 */
	@Override
	public boolean download(String localFile) {
		try {
			// Connect and update info
			URLConnection connection = connect();
			if (connection == null) return false;
			updateInfo(connection);

			// Copy resource to local file, use remote file if no local file name specified
			InputStream is = url.openStream();

			// Open local file
			if (verbose) Timer.showStdErr("Local file name: '" + localFile + "'");

			// Create local directory if it doesn't exists
			mkdirsLocal(localFile);
			FileOutputStream os = new FileOutputStream(localFile);

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
			Timer.showStdErr("ERROR while connecting to " + getUrl());
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getCanonicalPath() {
		return url.toString();
	}

	@Override
	public String getName() {
		File path = new File(url.getPath());
		return path.getName();
	}

	@Override
	public String getParent() {
		try {
			String path = url.getPath();
			String paren = (new File(path)).getParent();
			URI uri = new URI(url.getProtocol(), url.getAuthority(), paren, null, null);
			return uri.toString();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Error parsing URL: " + url, e);
		}
	}

	@Override
	public String getPath() {
		return url.getPath();
	}

	public URL getUrl() {
		return url;
	}

	/**
	 * HTTP has no concept of directory
	 */
	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public boolean isFile() {
		return true;
	}

	@Override
	public ArrayList<String> list() {
		return new ArrayList<>();
	}

	@Override
	protected String localPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Config.get().getTmpDir() + "/" + TMP_BDS_DATA);
		sb.append("/" + url.getProtocol());

		// Authority: Host and port
		if (url.getAuthority() != null) {
			for (String part : url.getAuthority().split("[:\\.]")) {
				if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
			}
		}

		// Path
		if (url.getPath() != null) {
			for (String part : url.getPath().split("/")) {
				if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
			}
		}

		// Query
		if (url.getQuery() != null) {
			for (String part : url.getQuery().split("&")) {
				if (!part.isEmpty()) sb.append("/" + Gpr.sanityzeName(part));
			}
		}

		return sb.toString();
	}

	/**
	 * Cannot create remote dirs in http
	 */
	@Override
	public boolean mkdirs() {
		return false;
	}

	protected URL parseUrl(String urlStr) {
		try {
			// No protocol: file
			if (urlStr.indexOf(PROTOCOL_SEP) < 0) return new URL("file" + PROTOCOL_SEP + urlStr);

			// Encode the url
			URIBuilder ub = new URIBuilder(urlStr);
			return ub.build().toURL();
		} catch (URISyntaxException | MalformedURLException e) {
			throw new RuntimeException("Cannot parse URL " + urlStr, e);
		}
	}

	/**
	 * Connect and update info
	 */
	@Override
	protected boolean updateInfo() {
		return updateInfo(connect());
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
	public boolean upload(String localFileName) {
		return false;
	}

}
