package org.bds.util;

import java.io.Serializable;
import java.util.Date;

public class Timer implements Serializable {

	private static final long serialVersionUID = 1749543702279403162L;

	private static Timer timer = new Timer(); // Keep track of time (since first class instantiation)

	Date start;
	Date end;
	long timeOut;

	/**
	 * Show absolute timer map and a message
	 * @param msg
	 */
	public static void show(String msg) {
		System.out.println(timer + "\t" + msg);
	}

	/**
	 * Show absolute timer map and a message on STDERR
	 * @param msg
	 */
	public static void showStdErr(String msg) {
		System.err.println(timer + "\t" + msg);
	}

	public static String toDDHHMMSS(long deltaMilliSecs) {
		long days = deltaMilliSecs / (24 * 60 * 60 * 1000);
		long hours = (deltaMilliSecs % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
		long mins = (deltaMilliSecs % (60 * 60 * 1000)) / (60 * 1000);
		long secs = (deltaMilliSecs % (60 * 1000)) / (1000);

		if (days > 0) {
			String s = "";
			if (days > 1) s = "s"; // More than one day? Then it's plural

			if ((hours + mins + secs) > 0) return String.format("%d day%s %02d:%02d:%02d", days, s, hours, mins, secs);
			return String.format("%d day%s", days, s); // All others are zero? Just say "1 day" instead of "1 day 00:00:00"
		}

		// Just HH:MM:SS
		return String.format("%02d:%02d:%02d", hours, mins, secs);
	}

	public static String toDDHHMMSSms(long delta) {
		long days = delta / (24 * 60 * 60 * 1000);
		long hours = (delta % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
		long mins = (delta % (60 * 60 * 1000)) / (60 * 1000);
		long secs = (delta % (60 * 1000)) / (1000);
		long ms = (delta % 1000);

		if (days > 0) {
			String s = "";
			if (days > 1) s = "s"; // More than one day? Then it's plural

			if ((hours + mins + secs + ms) > 0) return String.format("%d day%s %02d:%02d:%02d.%03d", days, s, hours, mins, secs, ms);
			return String.format("%d day%s", days, s); // All others are zero? Just say "1 day" instead of "1 day 00:00:00"
		}

		if (days > 0) return String.format("%d days %02d:%02d:%02d.%03d", days, hours, mins, secs, ms);
		return String.format("%02d:%02d:%02d.%03d", hours, mins, secs, ms);
	}

	/**
	 * Transform miliseconds to HH:MM:SS
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
		start = new Date();
		end = null;
	}

	/**
	 * Elapsed miliseconds
	 */
	public long elapsed() {
		if (end != null) return end.getTime() - start.getTime();
		Date now = new Date();
		return now.getTime() - start.getTime();
	}

	/**
	 * Elapsed seconds
	 */
	public int elapsedSecs() {
		long e = elapsed();
		return (int) (e / 1000);
	}

	public void end() {
		end = new Date();
	}

	public Date getEnd() {
		return end;
	}

	public Date getStart() {
		return start;
	}

	/**
	 * Has this timer expired?
	 */
	public boolean isExpired() {
		return timeOut <= elapsed();
	}

	/**
	 * Remaining time until timeout
	 */
	public long remaining() {
		long remaining = timeOut - elapsed();
		return remaining > 0 ? remaining : 0;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Timer start() {
		start = new Date();
		end = null;
		return this;
	}

	@Override
	public String toString() {
		return toDDHHMMSSms(elapsed());
	}
}
