package org.bds;

import org.bds.util.Timer;

/**
 * Log messages
 *
 * @author pcingola
 */
public interface BdsLog {

	/**
	 * Show a 'debug' message to STDERR
	 */
	default void debug(String message) {
		if (isDebug()) {
			Timer.showStdErr("DEBUG " + debugMessagePrepend() + ": " + message);
		}
	}

	/**
	 * This string is always prepended to debug messages
	 */
	default String debugMessagePrepend() {
		StackTraceElement ste = new Exception().getStackTrace()[2];
		String logmsg = logMessagePrepend();

		// Class where source code is executing (e.g. inherited method)
		String srcClass = ste.getClassName();
		int ind = srcClass.lastIndexOf('.');
		srcClass = srcClass.substring(ind + 1);

		// This class
		String thisClass = getClass().getSimpleName();

		return logmsg //
				+ (logmsg.isEmpty() ? "" : ", ") //
				+ (thisClass.equals(srcClass) ? "" : thisClass + " ") // Show class only if different than source code class
				+ srcClass + "." + ste.getMethodName() + ":" + ste.getLineNumber() //
		;
	}

	/**
	 * Show an 'error' message to STDERR
	 * @param message
	 */
	default void error(String message) {
		Timer.showStdErr("ERROR: " + message);
	}

	/**
	 * Debug mode?
	 * FIXME: Make sure we override this method in all classes that have a local 'debug' field
	 */
	default boolean isDebug() {
		return Config.get().isDebug();
	}

	/**
	 * Logging mode?
	 * FIXME: Make sure we override this method in all classes that have a local 'log' field
	 */
	default boolean isLog() {
		return Config.get().isLog();
	}

	/**
	 * Verbose mode?
	 * FIXME: Make sure we override this method in all classes that have a local 'verbose' field
	 */
	default boolean isVerbose() {
		return Config.get().isVerbose();
	}

	/**
	 * Show a 'log' message to STDERR
	 * @param message
	 */
	default void log(String message) {
		if (isVerbose()) {
			Timer.showStdErr("INFO " + logMessagePrepend() + ": " + message);
		}
	}

	/**
	 * This string is always prepended to log messages
	 */
	default String logMessagePrepend() {
		return "";
	}

	/**
	 * Show a 'warning' message to STDERR
	 * @param message
	 */
	default void warning(String message) {
		Timer.showStdErr("WARNING: " + message);
	}

}
