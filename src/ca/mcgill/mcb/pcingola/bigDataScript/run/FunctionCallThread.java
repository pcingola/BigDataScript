package ca.mcgill.mcb.pcingola.bigDataScript.run;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.FunctionCall;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Statement;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * A thread that performs a function call using some arguments
 * Typical example:
 *
 * 		par funct(x, y)
 *
 * The executions is:
 *	i) Function arguments x and y are calculated (in the calling thread, to avid race conditions)
 *  ii) A new thread is created
 *  iii) The new thread evaluates the function using the previously calculated arguments
 *
 * @author pcingola
 */
public class FunctionCallThread extends BigDataScriptThread {

	Object arguments[];

	public FunctionCallThread(Statement statement, BigDataScriptThread parent, Object arguments[]) {
		super(statement, parent);
		this.arguments = arguments;
		if (!(statement instanceof FunctionCall)) throw new RuntimeException("Cannot initialize using non FunctionCall");
	}

	/**
	 * Run statements (i.e. run function call)
	 */
	@Override
	protected RunState runStatement() {
		// Run program
		RunState runState = null;
		try {
			FunctionCall functionCall = (FunctionCall) statement;
			functionCall.eval(this, arguments);
			runState = RunState.OK;
		} catch (Throwable t) {
			runState = RunState.FATAL_ERROR;
			if (isVerbose()) throw new RuntimeException(t);
			else Timer.showStdErr("Fatal error: Program execution finished");
		}

		return runState;
	}

}
