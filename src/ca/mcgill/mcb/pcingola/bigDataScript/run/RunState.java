package ca.mcgill.mcb.pcingola.bigDataScript.run;

/**
 * Run state
 * @author pcingola
 */
public enum RunState {

	OK // Normal state
	, BREAK // Executing a 'break' statement (break from loop)
	, CHECKPOINT_RECOVER // Recovering from a checkpoint
	, CONTINUE // Executing a 'continue' statement (contuniue loop)
	, EXIT // Executing an 'exit' statement (exit program)
	, FATAL_ERROR // Finished executing due to a fatal error
	, RETURN // Executing a 'return' statement (return from function)
	, THREAD_KILLED // Thread was sent a 'kill' signla
	, WAIT_RECOVER // Recovering from a checkpoint, in a "wait" instruction
}
