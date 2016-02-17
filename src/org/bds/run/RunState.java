package org.bds.run;

/**
 * Run state: Thread execution state
 *
 * @author pcingola
 */
public enum RunState {

	OK // Normal state
	, BREAK // Executing a 'break' statement (break from loop)
	, CHECKPOINT_RECOVER // Recovering from a checkpoint
	, CONTINUE // Executing a 'continue' statement (continue loop)
	, EXIT // Executing an 'exit' statement (exit program)
	, FATAL_ERROR // Finished executing due to a fatal error
	, FINISHED // Thread execution completely finished
	, FROZEN // Frozen execution: running, but waiting for event (e.g. checkpoint or debugging)
	, RETURN // Executing a 'return' statement (return from function)
	, THREAD_KILLED // Thread was sent a 'kill' signal
	, WAIT_RECOVER // Recovering from a checkpoint, in a "wait" instruction
	;

	/**
	 * Are we in 'Checkpoint Recovery' mode?
	 */
	public boolean isCheckpointRecover() {
		return this == RunState.WAIT_RECOVER //
				|| this == RunState.CHECKPOINT_RECOVER //
		;
	}

	/**
	 * Should we exit the execution thread?
	 */
	public boolean isExit() {
		return this == EXIT //
				|| this == FATAL_ERROR //
				|| this == THREAD_KILLED //
		;
	}

	public boolean isFatalError() {
		return this == RunState.FATAL_ERROR;
	}

	public boolean isFinished() {
		return this == FINISHED;
	}

	/**
	 * Is the program frozen?
	 */
	public boolean isFrozen() {
		return this == FROZEN;
	}

	public boolean isReturn() {
		return this == RunState.RETURN;
	}

}
