package org.bds.run;

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * All BdsThreads are tracked here
 *
 * @author pcingola
 */
public class BdsThreads {

	public static boolean doNotRemoveThreads = false; // This is used in only for some test cases

	private static BdsThreads bdsThreadsInstance = new BdsThreads();

	BdsThread bdsThreadRoot = null;
	Map<Long, BdsThread> bdsThreadByThreadId = new HashMap<>();
	Set<BdsThread> bdsThreadDone = new HashSet<>();

	/**
	 * Get singleton
	 */
	public static BdsThreads getInstance() {
		return bdsThreadsInstance;
	}

	/**
	 * Reset singleton
	 */
	public static void reset() {
		bdsThreadsInstance = new BdsThreads();
	}

	/**
	 * Add a bdsThread
	 */
	public synchronized void add(BdsThread bdsThread) {
		long id = Thread.currentThread().getId();
		if (bdsThreadRoot == null && bdsThread.isRoot()) bdsThreadRoot = bdsThread;
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
	 * Get current bds thread if found, otherwise return root thread
	 */
	public synchronized BdsThread getOrRoot() {
		long id = Thread.currentThread().getId();
		BdsThread bdsThread = bdsThreadByThreadId.get(id);
		if (bdsThread != null) return bdsThread;
		return bdsThreadRoot;
	}

	public BdsThread getRoot() {
		return bdsThreadRoot;
	}

	/**
	 * Resolve un-serialization
	 */
	private Object readResolve() throws ObjectStreamException {
		bdsThreadsInstance = this; // Replace singleton instance
		return this;
	}

	/**
	 * Remove a bdsThread
	 */
	public synchronized void remove() {
		if (doNotRemoveThreads) return;

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
