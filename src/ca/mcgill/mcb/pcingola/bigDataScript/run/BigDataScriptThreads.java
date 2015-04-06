package ca.mcgill.mcb.pcingola.bigDataScript.run;

import java.util.HashMap;
import java.util.Map;

public class BigDataScriptThreads {

	private static BigDataScriptThreads bigDataScriptThreads = new BigDataScriptThreads();

	Map<Long, BigDataScriptThread> bdsThreadByThreadId = new HashMap<Long, BigDataScriptThread>();

	/**
	 * Get singleton
	 */
	public static BigDataScriptThreads getInstance() {
		return bigDataScriptThreads;
	}

	/**
	 * Reset singleton
	 */
	public static void reset() {
		bigDataScriptThreads = new BigDataScriptThreads();
	}

	/**
	 * Add a bdsThread
	 */
	public void add(BigDataScriptThread bdsThread) {
		long id = Thread.currentThread().getId();
		bdsThreadByThreadId.put(id, bdsThread);
	}

	/**
	 * Get bdsThread
	 */
	public BigDataScriptThread get() {
		long id = Thread.currentThread().getId();
		return bdsThreadByThreadId.get(id);
	}

	/**
	 * Remove a bdsThread
	 */
	public void remove(BigDataScriptThread bdsThread) {
		long id = Thread.currentThread().getId();
		if (bdsThreadByThreadId.get(id) == bdsThread) bdsThreadByThreadId.remove(id);
	}

}
