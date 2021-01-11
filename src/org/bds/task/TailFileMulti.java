package org.bds.task;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * A file to use with 'Tail'
 *
 * Allows to 'follow' thousands of files by not opening the files unless is needed
 * This is to avoid operating systems limits on number of opened file descriptors (see 'ulimit' unix command)
 *
 * @author pcingola
 */
public class TailFileMulti extends TailFile {

	private static final long serialVersionUID = -6234980266728470190L;

	long inputPos = 0; // Latest position read
	File inputFile;
	boolean exists = false;

	public TailFileMulti(String inputFileName, boolean showStderr) {
		super(inputFileName, showStderr);
		inputFile = new File(inputFileName);
	}

	@Override
	public synchronized void close() {
		close(true);
	}

	/**
	 * Close file
	 */
	@Override
	protected synchronized void close(boolean attemptTail) {
		try {
			if (attemptTail) tail();
		} catch (Exception e) {
			// Nothing to do
		}
	}

	/**
	 * Open a file and add buffer to 'buffers'
	 */
	@Override
	protected synchronized boolean open() {
		// We don't really open any file
		return true;
	}

	/**
	 * Check if there is output available on any file
	 * @returns Number of bytes read. Negative number of there were problems
	 */
	@Override
	protected int tail() {
		if (!exists) {
			exists = inputFile.exists();
			if (!exists) return 0;
		}

		// File size
		long size = inputFile.length();
		if (size <= inputPos) return 0;

		int avail = (int) (size - inputPos);
		int count = 0;
		try {
			// Open input
			FileInputStream fis = new FileInputStream(inputFileName);
			fis.skip(inputPos);
			BufferedInputStream input = new BufferedInputStream(fis);

			BufferedOutputStream output = null;
			avail = input.available();

			while (avail > 0) {
				// Read all available bytes
				avail = Math.min(avail, MAX_BUFFER_SIZE); // Limit buffer size (some systems return MAX_INT when the file is growing)
				byte[] bytes = new byte[avail];
				count = input.read(bytes);
				inputPos += count;

				// Show bytes
				if (showStderr) System.err.write(bytes, 0, count);
				else System.out.write(bytes, 0, count);

				// Write to output
				if (output != null) output.write(bytes, 0, count);

				avail = input.available(); // Something more to read?
			}

			// Close input and output
			input.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return count;
	}
}
