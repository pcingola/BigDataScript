package org.bds.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URLConnection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.zip.GZIPInputStream;

/**
 * General purpose routines
 */
public class Gpr {

	// Number of cores in this computer
	public static final int NUM_CORES = Runtime.getRuntime().availableProcessors();

	// User's home directory
	public static final String HOME = System.getProperty("user.home");

	/**
	 * Return file's name (without the path)
	 * @param file
	 */
	public static String baseName(String file) {
		if (file == null) return "";
		File f = new File(file);
		return f.getName();
	}

	/**
	 * Return file's name (without the path)
	 * @param file
	 */
	public static String baseName(String file, String ext) {
		File f = new File(file);
		String base = f.getName();
		if (base.endsWith(ext)) return base.substring(0, base.length() - ext.length());
		return base;
	}

	// Show a long as a 64 bit binary number
	public static String bin64(long l) {
		String bl = Long.toBinaryString(l);
		String out = "";
		for (int i = bl.length(); i < 64; i++)
			out += "0";
		return out + bl;
	}

	/**
	 * Can we read this file (either exact name or append a '.gz'
	 */
	public static boolean canRead(String fileName) {
		File inputFile = new File(fileName);
		if (inputFile.exists() && inputFile.canRead()) return true;

		inputFile = new File(fileName + ".gz");
		if (inputFile.exists() && inputFile.canRead()) return true;

		return false;
	}

	/**
	 * Compare if objects are null. If non-null, compare objects
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int compareNull(Comparable o1, Comparable o2) {
		int i1 = (o1 == null ? 0 : 1);
		int i2 = (o2 == null ? 0 : 1);
		int cmp = i1 - i2;
		if (cmp != 0) return cmp;

		return o1.compareTo(o2);
	}

	/**
	 * Return a time-stamp showing When was the JAR file
	 * created OR when was a class compiled
	 */
	public static String compileTimeStamp(Class<?> cl) {
		try {
			String resName = cl.getName().replace('.', '/') + ".class";
			URLConnection conn = ClassLoader.getSystemResource(resName).openConnection();

			long epoch = 0;
			if (conn instanceof JarURLConnection) {
				// Is it a JAR file? Get manifest time
				epoch = ((JarURLConnection) conn).getJarFile().getEntry("META-INF/MANIFEST.MF").getTime();
			} else {
				// Regular file? Get file compile time
				epoch = conn.getLastModified();
			}

			// Format as timestamp
			Date epochDate = new Date(epoch);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return df.format(epochDate);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Count lines in a file (same as 'wc -l file' in unix)
	 */
	public static int countLines(String file) {
		try {
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(file));
			int lines = 0;
			while (reader.readLine() != null)
				lines++;
			reader.close();
			return lines;
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Prints a debug message (prints class name, method and line number)
	 * @param obj					Object to print
	 * @param offset				Offset N lines from stacktrace
	 */
	public static void debug(Object obj) {
		debug(obj, 1, true);
	}

	public static void debug(Object obj, int offset) {
		debug(obj, offset, true);
	}

	public static void debug(Object obj, int offset, boolean newLine) {
		StackTraceElement ste = new Exception().getStackTrace()[1 + offset];
		String steStr = ste.getClassName();
		int ind = steStr.lastIndexOf('.');
		steStr = steStr.substring(ind + 1);
		steStr += "." + ste.getMethodName() + "(" + ste.getLineNumber() + "):\t" + (obj == null ? null : obj.toString());
		if (newLine) System.err.println(steStr);
		else System.err.print(steStr);
	}

	/**
	 * Return file's dir
	 */
	public static String dirName(String file) {
		File f = new File(file);
		return f.getParent();
	}

	/**
	 * Does 'file' exist?
	 */
	public static boolean exists(String file) {
		return new File(file).exists();
	}

	/**
	 * Get a file's extension (all letters after the last '.'
	 */
	public static String extName(String file) {
		String base = baseName(file);
		int idx = base.lastIndexOf('.');
		if (idx >= 0) return base.substring(idx + 1);
		return "";
	}

	public static File getCanonicalFile(File f) {
		try {
			return f.getCanonicalFile();
		} catch (Exception e) {
			return f;
		}
	}

	/**
	 * Canonical path as a string
	 */
	public static String getCanonicalFileName(File f) {
		try {
			return f.getCanonicalPath();
		} catch (Exception e) {
			return f.getPath();
		}
	}

	/**
	 * Canonical path as a string
	 */
	public static String getCanonicalFileName(String f) {
		try {
			File ff = new File(f);
			return ff.getCanonicalPath();
		} catch (Exception e) {
			return f;
		}
	}

	/**
	 * Equivalent to Boolean.parseBoolean, except it returns 0 on invalid integer (NumberFormatException)
	 * @param s
	 * @return	int
	 */
	public static boolean parseBoolSafe(String s) {
		try {
			if (s == null || s.isEmpty()) return false;
			if (s.equalsIgnoreCase("true")) return true; // 'true'
			if (s.equalsIgnoreCase("t")) return true; // Abreviation of 'true'
			if (Gpr.parseIntSafe(s) != 0) return true; // A non-zero number
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Equivalent to Double.parseDouble(), except it returns 0 on invalid double (NumberFormatException)
	 */
	public static double parseDoubleSafe(String s) {
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			return 0.0;
		}
	}

	/**
	 * Equivalent to Integer.parseInt, except it returns 0 on invalid integer (NumberFormatException)
	 */
	public static int parseIntSafe(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Equivalent to Long.parseLong, except it returns 0 on invalid integer (NumberFormatException)
	 */
	public static long parseLongSafe(String s) {
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Equivalent to Long.parseLong, except it returns 0 on invalid integer (NumberFormatException)
	 * It also allows numbers to be finished by 'K', 'M', 'G', etc. to identify (2^10, 2^20, 2^30, etc.)
	 */
	public static long parseMemSafe(String s) {

		s = s.trim().toUpperCase();
		if (s.isEmpty()) return 0L;

		// Find multiplier
		long mult = 1;
		if (s.endsWith("K")) mult = 1L << 10;
		else if (s.endsWith("M")) mult = 1L << 20;
		else if (s.endsWith("G")) mult = 1L << 30;
		else if (s.endsWith("T")) mult = 1L << 40;
		else if (s.endsWith("P")) mult = 1L << 50;

		// Remove last char
		if (mult != 1) s = s.substring(0, s.length() - 1).trim();

		try {
			return Long.parseLong(s) * mult;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Add 'preLine' to each line in 'str'
	 */
	public static String prependEachLine(String preLine, String str) {
		if (preLine == null || str == null) return null;

		StringBuilder sb = new StringBuilder();
		for (String line : str.split("\n"))
			sb.append(preLine + line + "\n");

		return sb.toString();

	}

	/**
	 * Read an input stream
	 */
	public static String read(InputStream is) {
		if (is == null) return null;
		StringBuffer strb = new StringBuffer();
		char buff[] = new char[10240];
		int len = 0;

		try {
			BufferedReader inFile = new BufferedReader(new InputStreamReader(is));
			while ((len = inFile.read(buff)) >= 0)
				strb.append(buff, 0, len);
			inFile.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return strb.toString();

	}

	/**
	 * Try to open a file (BufferedReader) using either the file or a gzip file (appending '.gz' to fileName)
	 */
	public static BufferedReader reader(String fileName) {
		return reader(fileName, false, true);
	}

	public static BufferedReader reader(String fileName, boolean gzip) {
		return reader(fileName, gzip, true);
	}

	/**
	 * Try to open a file (BufferedReader) using either the file or a gzip file (appending '.gz' to fileName)
	 * @param fileName
	 * @param gzip : If true, file is assumed to be gzipped
	 * @return
	 */
	public static BufferedReader reader(String fileName, boolean gzip, boolean showExceptions) {
		BufferedReader reader = null;

		try {
			if (fileName.equals("-")) {
				return new BufferedReader(new InputStreamReader(System.in));
			} else if (fileName.endsWith(".gz") || gzip) {
				// This is a gzip compressed file
				File inputFile = new File(fileName);
				if (inputFile.exists()) return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(new File(fileName)))));
				else throw new RuntimeException("File not found '" + fileName + "'");
			} else {
				// Try opening the file
				File inputFile = new File(fileName);
				if (inputFile.exists()) return new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
				else {
					// Doesn't exists? => append '.gz' the file's name and try gzipped file
					String fileNameGz = fileName + ".gz";
					inputFile = new File(fileNameGz);
					if (inputFile.exists()) return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(new File(fileNameGz)))));
					else {
						if (!showExceptions) {
							Gpr.debug("File not found '" + fileName + "'");
							return null;
						}
						throw new RuntimeException("File not found '" + fileName + "'");

					}
				}
			}
		} catch (FileNotFoundException e) {
			reader = null;
			Gpr.debug(e);
		} catch (IOException e) {
			reader = null;
			Gpr.debug(e);
		}

		return reader;
	}

	public static String readFile(String fileName) {
		return readFile(fileName, true);
	}

	/**
	 * Read a file as a String.
	 * Note: the file can be compressed using gzip (file name must have a ".gz" extension).
	 *
	 * @param fileName : File to read (null on error)
	 * @param showExceptions : show exceptions if true
	 */
	public static String readFile(String fileName, boolean showExceptions) {
		BufferedReader inFile;
		StringBuffer strb = new StringBuffer();
		char buff[] = new char[10240];
		int len = 0;
		try {
			// Open
			inFile = reader(fileName, false, showExceptions);
			if (inFile == null) return "";

			// Read
			while ((len = inFile.read(buff)) >= 0)
				strb.append(buff, 0, len);
			inFile.close();
		} catch (IOException e) {
			if (showExceptions) throw new RuntimeException(e);
			else return "";
		}
		return strb.toString();
	}

	public static String removeExt(String file) {
		int lastDot = file.lastIndexOf('.');
		if (lastDot >= 0) return file.substring(0, lastDot);
		return file;
	}

	/**
	 * Remove extension from a file (if matche one of 'fileExtensions[]')
	 * @param file
	 * @return
	 */
	public static String removeExt(String file, String fileExtensions[]) {
		for (String ext : fileExtensions)
			if (file.toLowerCase().endsWith(ext)) return file.substring(0, file.length() - ext.length());

		return file;
	}

	/**
	 * Convert a string to be used in a filename
	 */
	public static String sanityzeName(String fileName) {
		String out = fileName.trim().replaceAll("[^0-9_a-zA-Z\\.]", "_");
		out = out.replaceAll("_+", "_");

		// Remove all leading underscores
		while (out.startsWith("_") || out.startsWith("."))
			out = out.substring(1);

		// Remove all trailing underscores
		while (out.endsWith("_") || out.endsWith("."))
			out = out.substring(0, out.length() - 1);

		return out;
	}

	/**
	 * Write an object to a file
	 * @param fileName: File to write
	 * @param obj: Object
	 */
	public static String toFile(String fileName, Object obj) {
		return toFile(fileName, obj, false);
	}

	/**
	 * Write an object to a file
	 * @param fileName: File to write
	 * @param obj: Object
	 */
	public static String toFile(String fileName, Object obj, boolean append) {
		try {
			String str = obj.toString();
			FileWriter outFile = new FileWriter(fileName, append);
			outFile.write(str);
			outFile.flush();
			outFile.close();
			return str;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Convert 2 numbers to memory units [KB, MB, GB, TB, PB]
	 */
	public static String toStringMem(long memBytes) {
		double k = 1024, m = k * k, g = k * m, t = k * g, p = k * t;
		double mem = memBytes;

		String unit = "B";
		if (mem > p) {
			mem /= p;
			unit = "PB";
		} else if (mem > t) {
			mem /= t;
			unit = "TB";
		} else if (mem > g) {
			mem /= g;
			unit = "GB";
		} else if (mem > m) {
			mem /= m;
			unit = "MB";
		} else if (mem > k) {
			mem /= k;
			unit = "KB";
		}
		return String.format("%.1f %s", mem, unit);
	}
}
