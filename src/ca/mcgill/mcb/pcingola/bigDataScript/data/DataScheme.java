package ca.mcgill.mcb.pcingola.bigDataScript.data;

public enum DataScheme {
	FILE // Local file. Includes any file that can be seen locally by the file system (e.g. NFS is considered local)
	, HTTP // A file in a web server
	, HTTPS // A file in a secure web server
	, FTP // A file in an ftp server
	, S3 // An object in an Amazon S3 bucket
	, HDFS // A file in a Hadoop Distributed File System
	; //

	public static final String SEP = "://"; // Separation between scheme and path

	/**
	 *  Parse scheme
	 */
	public static DataScheme parse(String path) {
		if (path.indexOf(SEP) < 0) return FILE;

		// Find scheme
		String upath = path.toUpperCase();
		for (DataScheme s : DataScheme.values()) {
			String urlPrefix = s.toString() + SEP;
			if (upath.startsWith(urlPrefix)) return s;
		}

		// Nothing found
		return null;
	}

	public boolean isLocal() {
		return !isRemote();
	}

	/**
	 * Is this a remote file?
	 * Rule of the thumb: Anything that cannot be accessed
	 * by a simple "ls" is considered remote
	 */
	public boolean isRemote() {
		return this != FILE;
	}

}