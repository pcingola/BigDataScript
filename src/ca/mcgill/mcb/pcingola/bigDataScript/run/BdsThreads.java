package ca.mcgill.mcb.pcingola.bigDataScript.run;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BdsThreads {

	private static BdsThreads bdsThreads = new BdsThreads();

	Map<Long, BdsThread> bdsThreadByThreadId = new HashMap<Long, BdsThread>();
	Set<BdsThread> bdsThreadDone = new HashSet<BdsThread>();

	/**
	 * Get canonical path to file using thread's 'current dir' to de-reference
	 * relative paths
	 *
	 * Warning: When un-serializing a task form a checkpoint, threads are not
	 *          initialized, thus they are null
	 */
	public static String filePath(String fileName) {
		BdsThread bdsThread = BdsThreads.getInstance().get();
		return bdsThread != null ? bdsThread.dataLocalPath(fileName) : fileName;
	}

	/**
	 * Get singleton
	 */
	public static BdsThreads getInstance() {
		return bdsThreads;
	}

	/**
	 * Reset singleton
	 */
	public static void reset() {
		bdsThreads = new BdsThreads();
	}

	/**
	 * Add a bdsThread
	 */
	public synchronized void add(BdsThread bdsThread) {
		long id = Thread.currentThread().getId();
		bdsThreadByThreadId.put(id, bdsThread);
	}

	/**
	 * Get bdsThread
	 */
	public synchronized BdsThread get() {
		long id = Thread.currentThread().getId();
		return bdsThreadByThreadId.get(id);
	}

	/**
	 * Remove a bdsThread
	 */
	public synchronized void remove() {
		long id = Thread.currentThread().getId();
		BdsThread bdsThread = get();
		if (bdsThreadByThreadId.get(id) == bdsThread) {
			bdsThreadByThreadId.remove(id);
			bdsThreadDone.add(bdsThread);
		} else throw new RuntimeException("Cannot remove thread '" + bdsThread.getBdsThreadId() + "'");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Long thid : bdsThreadByThreadId.keySet())
			sb.append(thid + "\t" + bdsThreadByThreadId.get(thid).getBdsThreadId() + "\n");
		return sb.toString();
	}
}
