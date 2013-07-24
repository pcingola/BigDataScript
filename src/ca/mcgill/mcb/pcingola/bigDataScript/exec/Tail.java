package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A "tail -f" for java
 * 
 * Can 'follow' several files
 * If a file does not exist, tail waits until the file is created
 * 
 * @author pcingola
 */
public class Tail extends Thread {

	public static final int SLEEP_TIME_DEFAULT = 100;

	boolean running;
	HashMap<String, TailFile> files;
	HashSet<String> toRemove;

	public Tail() {
		files = new HashMap<String, TailFile>();
		toRemove = new HashSet<String>();
	}

	/**
	 * Add a file to follow
	 * @param inputFileName : Input stream to be followed
	 * @param outputFileName : Copy input to this file (can be null)
	 * @param showStderr : If true, print to STDERR
	 */
	public synchronized void add(InputStream input, String outputFileName, boolean showStderr) {
		TailFile tf = new TailFile(input, outputFileName, showStderr);
		files.put(outputFileName, tf);
	}

	/**
	 * Add a file to follow
	 * @param inputFileName : File to be followed (if null, it is ignored)
	 * @param outputFileName : Copy input to this file (can be null)
	 * @param showStderr : If true, print to STDERR
	 */
	public synchronized void add(String inputFileName, String outputFileName, boolean showStderr) {
		TailFile tf = new TailFile(inputFileName, outputFileName, showStderr);
		files.put(inputFileName, tf);
	}

	/**
	 * Close all files
	 */
	void close() {
		// Close all files
		for (TailFile tf : files.values())
			tf.close();
		files = new HashMap<String, TailFile>();
	}

	/**
	 * Kill this thread, stop 'following files'
	 */
	public void kill() {
		close();
		running = false;
	}

	/**
	 * Remove 'fileName' (do not 'follow' any more)
	 * @param fileName
	 * @param showStderr
	 */
	public synchronized void remove(String fileName) {
		try {
			TailFile tf = files.get(fileName);
			if (tf != null) {
				tf.close();
				files.remove(fileName);
			}
		} catch (Exception e) {
			// Nothing to do
		}
	}

	@Override
	public void run() {
		try {
			running = true;

			// Loop until kill()
			while (running) {
				tail();
				sleep(SLEEP_TIME_DEFAULT);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close();
		}
	}

	/**
	 * Check if there is output available on any file
	 */
	synchronized boolean tail() {
		boolean anyOutput = false;

		// Try to read form all buffers
		for (String name : files.keySet()) {
			TailFile tf = files.get(name);

			// Try to 'tail'. Any problems? => Remove the entry
			int bytes = tf.tail();

			if (bytes < 0) toRemove.add(name); // Problems? Remove the file from this list 
			else if (bytes > 0) anyOutput = true; // There was an output of 'bytes' number of bytes
		}

		// Remove problematic entries (if any)
		if (!toRemove.isEmpty()) {
			for (String fileName : toRemove) {
				TailFile tf = files.get(fileName);
				tf.close();
				files.remove(fileName);
			}
			toRemove = new HashSet<String>();
		}

		return anyOutput;
	}
}
