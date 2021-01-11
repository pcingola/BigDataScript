package org.bds;

/**
 * Logger messages
 *
 * @author pcingola
 */
public class BdsLogger {

	private static Logger logger = new Logger();

	public static void debug(Object msg) {
		logger.debug(msg);
	}

	public static void log(Object msg) {
		logger.log(msg);
	}

	public static void warning(Object msg) {
		logger.warning(msg);
	}

}

class Logger implements BdsLog {
	// This class is just to have the default 'log' capabilities from BdsLog interface
	@Override
	public int debugMessagePrependOffset() {
		return 3;
	}

};
