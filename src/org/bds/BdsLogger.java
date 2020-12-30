package org.bds;

/**
 * Logger messages
 *
 * @author pcingola
 */
public class BdsLogger {

	private static Logger logger = new Logger();

	public static void debug(String msg) {
		logger.debug(msg);
	}

	public static void log(String msg) {
		logger.log(msg);
	}

}

class Logger implements BdsLog {
	// This class is just to have the default 'log' capabilities from BdsLog interface
};
