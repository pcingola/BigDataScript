package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * A file to use with 'Tail'
 * Opens files, so it has limitations depending on the number of opened file descriptors allowed by the ssystem.
 * 
 * @author pcingola
 */
public class TailFileLocal extends TailFile {

	public static final int MAX_BUFFER_SIZE = 1024 * 1024;

	BufferedInputStream input; // Input buffer 
	BufferedOutputStream output; // Output buffer

	/**
	 * Provide an inputStream (instead of an input file)
	 * @param input
	 * @param outputFileName
	 */
	public TailFileLocal(InputStream input, String outputFileName, boolean showStderr) {
		super(null, outputFileName, showStderr);
		this.input = new BufferedInputStream(input);
	}

	public TailFileLocal(String inputFileName, String outputFileName, boolean showStderr) {
		super(inputFileName, outputFileName, showStderr);
	}

	/**
	 * Close files
	 */
	@Override
	protected synchronized void close(boolean attemptTail) {
		try {
			if (attemptTail) tail();

			// Is it already open?
			if (input != null) input.close();
			if (output != null) output.close();

			input = null;
			output = null;
		} catch (Exception e) {
			// Nothing to do
		}
	}

	/**
	 * Open a file and add buffer to 'buffers'
	 * @param inputFileName
	 * @return
	 */
	@Override
	protected synchronized boolean open() {
		try {
			// No input? Open it
			if (input == null) {
				if (!Gpr.exists(inputFileName)) return false; // File does not exists yet, it may be created later
				input = new BufferedInputStream(new FileInputStream(inputFileName));
			}

			// We have to open output?
			if ((outputFileName != null) && (output == null)) output = new BufferedOutputStream(new FileOutputStream(outputFileName));
		} catch (Exception e) {
			close(false);
			throw new RuntimeException(e);
		}

		return true;
	}

	/**
	 * Check if there is output available on any file
	 * @returns Number of bytes read. Negative number of there were problems
	 */
	@Override
	protected int tail() {
		if (!open()) return 0; // Files not opened yet (may be input file does not exists). OK, nothing to do...

		try {
			int count = 0;

			// Any bytes available on this buffer?
			int avail = input.available();
			if (avail > 0) {
				avail = Math.min(avail, MAX_BUFFER_SIZE); // Limit buffer size (some systems return MAX_INT when the file is growing)

				// Read all available bytes
				byte[] bytes = new byte[avail];
				count = input.read(bytes);

				// Show bytes
				if (showStderr) System.err.write(bytes, 0, count);
				else System.out.write(bytes, 0, count);

				// Write to output
				if (output != null) output.write(bytes, 0, count);
			}

			return count;
		} catch (Exception e) {
			// Problems with this buffer? Remove it from the list
			// Timer.showStdErr("ERROR: Tail on file '" + inputFileName + "' / '" + outputFileName + "' failed.\n" + e);
			close(false);
			return -1;
		}
	}
}
