package org.bds.task;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import org.bds.BdsLog;

/**
 * A "tail -f" for java
 *
 * Can 'follow' several files
 * If a file does not exist, tail waits until the file is created
 *
 * @author pcingola
 */
public class Tail extends Thread implements Serializable, BdsLog {

	private static final long serialVersionUID = 3971573486965684116L;

	public static final int SLEEP_TIME_DEFAULT = 100;

	boolean debug, verbose, quiet;
	boolean running;
	HashMap<String, TailFile> files;
	HashSet<String> toRemove;

	public Tail() {
		files = new HashMap<>();
		toRemove = new HashSet<>();
		setDaemon(true);
	}

	/**
	 * Add a file to follow
	 * @param inputFileName : Input stream to be followed
	 * @param tailId : Identifies this 'tail'
	 * @param showStderr : If true, print to STDERR
	 */
	public synchronized void add(InputStream input, String tailId, boolean showStderr) {
		if (quiet) return; // Quiet mode? Nothing to do

		TailFile tf = new TailStream(input, showStderr, tailId);
		debug("Adding (" + tf.getClass().getSimpleName() + ") '" + tailId + "'");
		tf.setDebug(debug);
		tf.setVerbose(verbose);
		files.put(tailId, tf);
	}

	/**
	 * Add a file to follow
	 * @param inputFileName : File to be followed (if null, it is ignored)
	 * @param outputFileName : Copy input to this file (can be null)
	 * @param showStderr : If true, print to STDERR
	 */
	public synchronized void add(String inputFileName, boolean showStderr) {
		if (inputFileName == null) return;
		if (quiet) return; // Quiet mode? Nothing to do

		TailFile tf = new TailFileMulti(inputFileName, showStderr);
		debug("Adding (" + tf.getClass().getSimpleName() + ") '" + inputFileName + "'");
		tf.setDebug(debug);
		tf.setVerbose(verbose);
		files.put(inputFileName, tf);
	}

	/**
	 * Close all files
	 */
	void close() {
		debug("Closing all files");

		// Close all files
		for (TailFile tf : files.values())
			tf.close();
		files = new HashMap<>();
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

	@Override
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Kill this thread, stop 'following files'
	 */
	public void kill() {
		debug("Killed");
		close();
		running = false;
	}

	/**
	 * Remove 'fileName' (do not 'follow' any more)
	 */
	public synchronized void remove(String fileName) {
		try {
			TailFile tf = files.get(fileName);
			if (tf != null) {
				debug("Removing (" + tf.getClass().getSimpleName() + ") '" + fileName + "'");
				tf.close();
			}
			files.remove(fileName);
		} catch (Exception e) {
			// Nothing to do
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			running = true;

			// Loop until kill()
			while (running) {
				if (!quiet) tail();
				sleep(SLEEP_TIME_DEFAULT);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close();
		}
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setQuiet(boolean quiet) {
		this.quiet = quiet;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
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
			int bytes = (tf != null ? tf.tail() : -1);

			if (bytes < 0) toRemove.add(name); // Problems? Remove the file from this list
			else if (bytes > 0) anyOutput = true; // There was an output of 'bytes' number of bytes
		}

		// Remove  entries (if any)
		if (!toRemove.isEmpty()) {
			for (String fileName : toRemove)
				remove(fileName);
			toRemove = new HashSet<>();
		}

		return anyOutput;
	}
}
