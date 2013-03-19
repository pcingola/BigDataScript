package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

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
	HashSet<String> fileNames;
	HashMap<String, BufferedInputStream> buffers;
	HashSet<String> isStderr;
	ArrayList<String> toRemove;

	public Tail() {
		fileNames = new HashSet<String>();
		buffers = new HashMap<String, BufferedInputStream>();
		isStderr = new HashSet<String>();
		toRemove = new ArrayList<String>();
	}

	/**
	 * Add a file to follow
	 * @param fileName : File to be followed (if null, it is ignored)
	 * @param showStderr : If true, print to STDERR
	 */
	public synchronized void add(String fileName, boolean showStderr) {
		if (fileName == null) return;
		fileNames.add(fileName);
		if (showStderr) isStderr.add(fileName);
	}

	/**
	 * Kill this thread, stop 'following files'
	 */
	public void kill() {
		running = false;
	}

	/**
	 * Attempt to open all files in 'fileNames'
	 */
	synchronized void open() {
		if (fileNames.isEmpty()) return;

		// Try to open all files
		ArrayList<String> done = new ArrayList<String>();
		for (String f : fileNames)
			if (open(f)) done.add(f);

		// Remove successfully opened files from the list
		for (String f : done)
			fileNames.remove(f);
	}

	/**
	 * Open a file and add buffer to 'buffers'
	 * @param fileName
	 * @return
	 */
	synchronized boolean open(String fileName) {
		if (!Gpr.exists(fileName)) return false; // File does not exists yet, it may be created later 

		BufferedInputStream buffer = null;
		try {
			buffer = new BufferedInputStream(new FileInputStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		buffers.put(fileName, buffer);
		return true;
	}

	/**
	 * Remove 'fileName' (do not 'follow' any more)
	 * @param fileName
	 * @param showStderr
	 */
	public synchronized void remove(String fileName) {
		try {
			// Remove from isStderr
			isStderr.remove(fileName);

			// Not opened => Only remove here
			if (fileNames.remove(fileName)) return;

			// Is it already open?
			BufferedInputStream bis = buffers.get(fileName);
			if (bis != null) {
				buffers.remove(fileName);
				bis.close();
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
				if (!tail()) sleep(SLEEP_TIME_DEFAULT); // No output? sleep a bit
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			// Close all files
			for (BufferedInputStream bis : buffers.values())
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * Check if there is output available on any file
	 */
	synchronized boolean tail() {
		open(); // Any files pending to be opened?

		// Try to read form all buffers
		for (String fileName : buffers.keySet()) {
			BufferedInputStream bis = buffers.get(fileName);

			try {
				// Any bytes available on this buffer?
				int avail = bis.available();
				if (avail > 0) {
					// Read all available bytes
					byte[] bytes = new byte[avail];
					bis.read(bytes);

					// Show bytes
					String str = new String(bytes);
					if (isStderr.contains(fileName)) System.err.print(str);
					else System.out.print(str);
				}
			} catch (Exception e) {
				// Problems with this buffer? Remove it from the list
				Timer.showStdErr("ERROR: Tail on file '' failed.\n" + e);
				toRemove.add(fileName);
			}
		}

		// Remove problematic buffers (if any)
		if (!toRemove.isEmpty()) {
			for (String fileName : toRemove)
				buffers.remove(fileName);
			toRemove = new ArrayList<String>();
		}

		return false;
	}
}
