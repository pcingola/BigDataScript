package org.bds.run;

import org.bds.Config;
import org.bds.lang.statement.FunctionCall;
import org.bds.lang.statement.Statement;
import org.bds.lang.value.ValueArgs;
import org.bds.util.Timer;

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
public class FunctionCallThread extends BdsThread {

	//	String functionCallNodeId; // Statement's ID, used only when un-serializing
	FunctionCall functionCall;
	ValueArgs arguments;

	public FunctionCallThread(Config config) {
		super(null, config);
	}

	public FunctionCallThread(Statement statement, FunctionCall functionCall, BdsThread parent, ValueArgs arguments) {
		super(statement, parent);
		setFunctionCall(functionCall);
		this.arguments = arguments;
	}

	@Override
	public void checkpointRecoverReset() {
		super.checkpointRecoverReset(); // Find the 'par' statement

		// Now we need to move to the first statement in the function (i.e. right after the function call)
		pc.checkpointRecoverFound();
	}

	/**
	 * Run statements (i.e. run function call)
	 */
	@Override
	protected void runStatement() {
		try {
			// Run function call
			functionCall.apply(this, arguments);
		} catch (Throwable t) {
			runState = RunState.FATAL_ERROR;
			if (isVerbose()) throw new RuntimeException(t);
			else Timer.showStdErr("Fatal error: Program execution finished");
		}
	}

	public void setFunctionCall(FunctionCall functionCall) {
		this.functionCall = functionCall;
		//		functionCallNodeId = functionCall.getNodeId();
	}

	//	@Override
	//	public void setStatement(Map<String, BdsSerialize> nodesById) {
	//		// Find and set functionCall
	//		FunctionCall fcall = (FunctionCall) nodesById.get(functionCallNodeId);
	//		if (fcall == null) throw new RuntimeException("Cannot find statement node '" + fcall + "'");
	//		setFunctionCall(fcall);
	//
	//		// Find and set statement
	//		String statId = getStatementNodeId();
	//		Statement stat = (Statement) nodesById.get(statId);
	//		if (stat == null) throw new RuntimeException("Cannot find statement node '" + statId + "'");
	//		setStatement(stat);
	//	}

}
