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
	default void debug(Object message) {
		if (isDebug()) {
			Timer.showStdErr("DEBUG " + debugMessagePrepend() + ": " + (message != null ? message.toString() : "null"));
		}
	}

	/**
	 * This string is always prepended to debug messages
	 */
	default String debugMessagePrepend() {
		StackTraceElement ste = new Exception().getStackTrace()[debugMessagePrependOffset()];
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

	default int debugMessagePrependOffset() {
		return 2;
	}

	/**
	 * Show an 'error' message to STDERR
	 * @param message
	 */
	default void error(Object message) {
		Timer.showStdErr("ERROR: " + (message != null ? message.toString() : "null"));
	}

	/**
	 * Debug mode?
	 */
	default boolean isDebug() {
		return Config.get().isDebug();
	}

	/**
	 * Logging mode?
	 */
	default boolean isLog() {
		return Config.get().isLog();
	}

	/**
	 * Verbose mode?
	 */
	default boolean isVerbose() {
		return Config.get().isVerbose();
	}

	/**
	 * Show a 'log' message to STDERR
	 * @param message
	 */
	default void log(Object message) {
		if (isVerbose()) {
			Timer.showStdErr("INFO " + logMessagePrepend() + ": " + (message != null ? message.toString() : "null"));
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
	default void warning(Object message) {
		Timer.showStdErr("WARNING: " + (message != null ? message.toString() : "null"));
	}

}
