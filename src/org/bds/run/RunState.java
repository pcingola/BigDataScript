package org.bds.run;

/**
 * Run state: Thread execution state
 *
 * @author pcingola
 */
public enum RunState {

	OK // Normal state
	, FATAL_ERROR // Finished executing due to a fatal error
	, FINISHED // Thread execution completely finished (all tasks and threads also finished execution)
	, THREAD_KILLED // Thread was sent a 'kill' signal
	;

	public boolean isFatalError() {
		return this == FATAL_ERROR;
	}

	public boolean isFinished() {
		return this == FINISHED;
	}

	public boolean isOk() {
		return this == OK;
	}

}
