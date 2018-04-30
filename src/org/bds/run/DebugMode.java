package org.bds.run;

public enum DebugMode {

	RUN, // Run until next breakpoint
	RUN_DEBUG, // Run until next 'debug' (ignore breakpoints)
	STEP, // Step one expression
	STEP_OVER, // Step until next return

}
