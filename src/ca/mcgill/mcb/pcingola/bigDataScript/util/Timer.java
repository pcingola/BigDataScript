package ca.mcgill.mcb.pcingola.bigDataScript.util;

import java.util.Date;

public class Timer {

	private static Timer timer = new Timer(); // Keep track of time (since first class instantiation)

	Date start;
	Date end;
	long timeOut;

	/**
	 * Show absolute timer value and a message
	 * @param msg
	 */
	public static void show(String msg) {
		System.out.println(timer + "\t" + msg);
	}

	/**
	 * Show absolute timer value and a message on STDERR
	 * @param msg
	 */
	public static void showStdErr(String msg) {
		System.err.println(timer + "\t" + msg);
	}

	/**
	 * Transform miliseconds to HH:MM:SS
	 * @param millisecs
	 * @return
	 */
	public static String toHHMMSS(long millisecs) {
		long hours = (millisecs) / (60 * 60 * 1000);
		long mins = (millisecs % (60 * 60 * 1000)) / (60 * 1000);
		long secs = (millisecs % (60 * 1000)) / (1000);
		return String.format("%02d:%02d:%02d", hours, mins, secs);
	}

	public Timer() {
		start = new Date();
	}

	public Timer(long timeOutMiliSec) {
		timeOut = timeOutMiliSec;
	}

	/**
	 * Elapsed miliseconds
	 * @return
	 */
	public long elapsed() {
		if (end != null) return end.getTime() - start.getTime();
		Date now = new Date();
		return now.getTime() - start.getTime();
	}

	/**
	 * Elapsed seconds
	 * @return
	 */
	public int elapsedSecs() {
		long e = elapsed();
		return (int) (e / 1000);
	}

	public void end() {
		end = new Date();
	}

	/**
	 * Has this timer expired?
	 * @return
	 */
	public boolean isExpired() {
		return timeOut <= elapsed();
	}

	/**
	 * Remaining time until timeout
	 * @return
	 */
	public long remaining() {
		long remaining = timeOut - elapsed();
		return remaining > 0 ? remaining : 0;
	}

	public void start() {
		start = new Date();
		end = null;
	}

	@Override
	public String toString() {
		long delta = elapsed();
		long days = delta / (24 * 60 * 60 * 1000);
		long hours = (delta % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
		long mins = (delta % (60 * 60 * 1000)) / (60 * 1000);
		long secs = (delta % (60 * 1000)) / (1000);
		long ms = (delta % 1000);

		if (days > 0) return String.format("%d days %02d:%02d:%02d.%03d", days, hours, mins, secs, ms);
		return String.format("%02d:%02d:%02d.%03d", hours, mins, secs, ms);
	}
}
