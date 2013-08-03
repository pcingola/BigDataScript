package ca.mcgill.mcb.pcingola.bigDataScript.exec;

/**
 * A file to use with 'Tail'
 * 
 * @author pcingola
 */
public abstract class TailFile {

	public static final int MAX_BUFFER_SIZE = 1024 * 1024;

	String inputFileName; // Read (tail -f) frmo this file
	String outputFileName; // Write to this file 
	boolean showStderr; // Do we show on STDERR? (default STDOUT)

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
	protected abstract void close(boolean attemptTail);

	/**
	 * Open a file and add buffer to 'buffers'
	 * @param inputFileName
	 * @return
	 */
	protected abstract boolean open();

	/**
	 * Check if there is output available on any file
	 * @returns Number of bytes read. Negative number of there were problems
	 */
	protected abstract int tail();
}
