package org.bds.task;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bds.BdsLog;
import org.bds.util.Gpr;

/**
 * A file to use with 'Tail -f' (i.e. follow file's grow)
 *
 * @author pcingola
 */
public abstract class TailFile implements Serializable, BdsLog {

	public static final int DEFAULT_TAIL = 10;
	public static final int MAX_BUFFER_SIZE = 1024 * 1024;
	private static final long serialVersionUID = -3331375637614242861L;

	boolean debug, verbose;
	String inputFileName; // Read (tail -f) from this file
	boolean showStderr; // Do we show on STDERR? (default STDOUT)

	/**
	 * Is there a newline in the buffer at position  'idx'?
	 * Note: If we have a process that shows 'progress lines' (e.g. terminated by '\r') we
	 *       don't want to show one long line, we just want to see the latest 'lines' produced.
	 */
	private static boolean isNewLine(byte[] buf, int idx, int idxMax) {
		if (buf[idx] == '\n') return true; // Lines terminated by '\n'
		if (idx < idxMax && buf[idx] == '\r' && buf[idx + 1] != '\n') return true; // Lines terminated by '\r' (e.g. progress lines in 'wget')
		return false;

	}

	public static String tail(String fileName) {
		return tail(fileName, DEFAULT_TAIL);
	}

	/**
	 * This is the typical 'tail' command behavior: Show the last 'n' lines of a file
	 *
	 * References: http://stackoverflow.com/questions/6888001/java-code-for-tail-n-lines-of-file-equivalent-to-tail-commad-in-unix
	 *
	 * @param fileName
	 * @param numLines : Number of line to read. Negative means "ALL lines"
	 * @return
	 */
	public static String tail(String fileName, int linesToRead) {
		if (linesToRead == 0) return "";

		// Read the whole file?
		if (linesToRead < 0) return Gpr.readFile(fileName);

		if (fileName == null) return null;
		File file = new File(fileName);
		if (file == null || !file.exists()) return null;
		if (file.length() <= 0) return "";

		// Read file
		final int chunkSize = 1024 * 64;
		List<String> lines = new ArrayList<>();
		StringBuilder latestLine = null;
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "r");

			long end = raf.length();
			boolean readMore = true;
			while (readMore) {
				byte[] buf = new byte[chunkSize];

				// Read a chunk from the end of the file
				long startPoint = end - chunkSize;
				long readLen = chunkSize;
				if (startPoint < 0) {
					readLen = chunkSize + startPoint;
					startPoint = 0;
				}

				// Read
				raf.seek(startPoint);
				readLen = raf.read(buf, 0, (int) readLen);
				if (readLen <= 0) break;

				// Parse newlines and add them to an array
				int unparsedSize = (int) readLen;
				int indexMax = unparsedSize - 1;
				int index = indexMax;
				while (index >= 0) {
					if (isNewLine(buf, index, indexMax)) {
						int startOfLine = index + 1;
						int len = (unparsedSize - startOfLine);
						if (len >= 0) {
							lines.add(new String(buf, startOfLine, len) + (latestLine != null ? latestLine.toString() : ""));
							latestLine = null;
						}
						unparsedSize = index + 1;
					}
					--index;
				}

				if (unparsedSize > 0) {
					if (latestLine == null) latestLine = new StringBuilder();
					latestLine.insert(0, new String(buf, 0, unparsedSize));
				}

				// Move end point back by the number of lines we parsed
				// Note: We have not parsed the first line in the chunked
				// content because could be a partial line
				end = end - chunkSize;

				readMore = (lines.size() <= linesToRead) && (startPoint != 0);
			}

			raf.close();
		} catch (Exception e) {
			throw new RuntimeException("Error tail on file '" + fileName + "'", e);
		}

		// Add 'latest' line
		if (latestLine != null) lines.add(latestLine.toString());

		// Only return the requested number of lines
		StringBuilder sb = new StringBuilder();
		int max = Math.min(linesToRead, lines.size() - 1);
		for (int i = max; i >= 0; i--)
			sb.append(lines.get(i));

		return sb.toString();
	}

	public TailFile(String inputFileName, boolean showStderr) {
		this.inputFileName = inputFileName;
		this.showStderr = showStderr;
	}

	public synchronized void close() {
		close(true);
	}

	/**
	 * Close files
	 */
	protected abstract void close(boolean attemptTail);

	@Override
	public boolean isDebug() {
		return debug;
	}

	@Override
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Open a file and add buffer to 'buffers'
	 */
	protected abstract boolean open();

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Check if there is output available on any file
	 * @returns Number of bytes read. Negative number of there were problems
	 */
	protected abstract int tail();
}
