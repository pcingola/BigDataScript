package ca.mcgill.mcb.pcingola.bigDataScript.run;

import java.util.HashMap;
import java.util.Map;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

public class BigDataScriptThreads {

	private static BigDataScriptThreads bigDataScriptThreads = new BigDataScriptThreads();

	Map<Long, BigDataScriptThread> bdsThreadByThreadId = new HashMap<Long, BigDataScriptThread>();

	protected static int REPORT_INTERVAL=20;
	protected static Timer timerReport;

	public static void createReport() {
		if ( timerReport==null) timerReport = new Timer();

		if ( timerReport.elapsedSecs() > REPORT_INTERVAL ) {
			//BigDataScriptThread thread = bigDataScriptThreads.get().getRoot(); // some DEADLOCK issue
			BigDataScriptThread thread = bigDataScriptThreads.get();

			// Create HTML report?
			if (thread.getConfig().isReportHtml()) {
				Report report = new Report(thread, false);
				report.createReport();
			}

			// Create YAML report?
			if (thread.getConfig().isReportYaml()) {
				Report report = new Report(thread, true);
				report.createReport();
			}

			// Reset report timer
			timerReport.start();
		}
	}
	/*
	protected static Thread threadReport;
	private BigDataScriptThreads() {
		// start report thread
		threadReport = new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(100);
					}
				}
				catch(InterruptedException e) {
					System.out.println("RuntimeException from threadReport.run()");
				}
			}
		};
		threadReport.start();
	}
	*/
	/**
	 * Get canonical path to file using thread's 'current dir' to de-reference
	 * relative paths
	 *
	 * Warning: When un-serializing a task form a checkpoint, threads are not
	 *          initialized, thus they are null
	 */
	public static String filePath(String fileName) {
		BigDataScriptThread bdsThread = BigDataScriptThreads.getInstance().get();
		return bdsThread != null ? bdsThread.filePath(fileName) : fileName;
	}

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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Long thid : bdsThreadByThreadId.keySet())
			sb.append(thid + "\t" + bdsThreadByThreadId.get(thid).getBdsThreadId() + "\n");
		return sb.toString();
	}
}
