package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * A file to use with 'Tail'
 * 
 * @author pcingola
 */
public class TailFile extends Thread {

	String inputFileName; // Read (tail -f) frmo this file
	String outputFileName; // Write to this file 
	BufferedInputStream input; // Input buffer 
	BufferedOutputStream output; // Output buffer
	boolean showStderr; // Do we show on STDERR? (default STDOUT)

	/**
	 * Provide an inputStream (instead of an input file)
	 * @param input
	 * @param outputFileName
	 */
	public TailFile(InputStream input, String outputFileName, boolean showStderr) {
		this.input = new BufferedInputStream(input);
		this.outputFileName = outputFileName;
		this.showStderr = showStderr;
	}

	public TailFile(String inputFileName, String outputFileName, boolean showStderr) {
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.showStderr = showStderr;
	}

	public synchronized void close() {
		close(true);
	}

	/**
	 * Close files
	 */
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
	synchronized boolean open() {
		try {
			// No input? Open it
			if (input == null) {
				if (!Gpr.exists(inputFileName)) return false; // File does not exists yet, it may be created later
				input = new BufferedInputStream(new FileInputStream(inputFileName));
			}

			// We have to open output?
			if ((outputFileName != null) && (output == null)) output = new BufferedOutputStream(new FileOutputStream(outputFileName));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return true;
	}

	/**
	 * Check if there is output available on any file
	 */
	protected boolean tail() {
		if (!open()) return true; // Files not opened yet (may be input file does not exists). OK, nothing to do...

		try {
			// Any bytes available on this buffer?
			int avail = input.available();
			if (avail > 0) {
				// Read all available bytes
				byte[] bytes = new byte[avail];
				input.read(bytes);

				// Show bytes
				String str = new String(bytes);
				if (showStderr) System.err.print(str);
				else System.out.print(str);

				// Write to output
				if (output != null) output.write(bytes);
			}

			return true;
		} catch (Exception e) {
			// Problems with this buffer? Remove it from the list
			// Timer.showStdErr("ERROR: Tail on file '" + inputFileName + "' / '" + outputFileName + "' failed.\n" + e);
			close(false);
		}

		return false;
	}
}
