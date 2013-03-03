package ca.mcgill.mcb.pcingola.bigDataScript.run;

/**
 * Run state
 * @author pcingola
 */
public enum RunState {

	OK // Normal state
	, BREAK // Executing a 'break' statement (break from loop)
	, CONTINUE // Executing a 'continue' statement (contuniue loop)
	, RETURN // Executing a 'return' statement (return from function)
	, EXIT // Executing an 'exit' statement (exit program)
	, CHECKPOINT_RECOVER // Recovering from a checkpoint
	, WAIT_RECOVER // Recovering from a checkpoint, in a "wait" instruction
	, TASK_END // Finished executing a 'task' statement
}
