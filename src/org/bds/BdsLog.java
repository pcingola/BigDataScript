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
		return logMessagePrepend() + ", " + getClass().getSimpleName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
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
