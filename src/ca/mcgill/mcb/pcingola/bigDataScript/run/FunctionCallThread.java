package ca.mcgill.mcb.pcingola.bigDataScript.run;

import java.util.Map;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.FunctionCall;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Statement;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerialize;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
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

	String functionCallNodeId; // Statement's ID, used only when un-serializing
	FunctionCall functionCall;
	Object arguments[];

	public FunctionCallThread(Config config) {
		super(null, config);
	}

	public FunctionCallThread(Statement statement, FunctionCall functionCall, BigDataScriptThread parent, Object arguments[]) {
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

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		super.serializeParse(serializer);
		functionCallNodeId = serializer.getNextFieldString();

		// Arguments encoded as base64 object
		String b64 = serializer.getNextField();
		arguments = (Object[]) serializer.base64Decode(b64);
	}

	/**
	 * Save thread's main information
	 */
	@Override
	protected String serializeSaveThreadMain(BigDataScriptSerializer serializer) {
		StringBuilder sb = new StringBuilder();
		sb.append(super.serializeSaveThreadMain(serializer));

		// Function call (nodeId)
		sb.append("\t");
		sb.append(serializer.serializeSaveValue(functionCallNodeId));

		// Arguments encoded as base64 object
		sb.append("\t");
		String b64 = serializer.base64encode(arguments);
		sb.append(b64);

		return sb.toString();
	}

	public void setFunctionCall(FunctionCall functionCall) {
		this.functionCall = functionCall;
		functionCallNodeId = functionCall.getNodeId();
	}

	@Override
	public void setStatement(Map<String, BigDataScriptSerialize> nodesById) {
		// Find and set functionCall
		FunctionCall fcall = (FunctionCall) nodesById.get(functionCallNodeId);
		if (fcall == null) throw new RuntimeException("Cannot find statement node '" + fcall + "'");
		setFunctionCall(fcall);

		// Find and set statement
		String statId = getStatementNodeId();
		Statement stat = (Statement) nodesById.get(statId);
		if (stat == null) throw new RuntimeException("Cannot find statement node '" + statId + "'");
		setStatement(stat);
	}

}
