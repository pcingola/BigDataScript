package org.bds.run;

import org.bds.Config;
import org.bds.lang.statement.FunctionCall;
import org.bds.lang.statement.Statement;
import org.bds.lang.value.ValueArgs;

/**
 * A thread that performs a function call using some arguments
 * Typical example:
 *
 * 		par function(x, y)
 *
 * The executions is:
 *	i) Function arguments x and y are calculated (in the calling thread, to avid race conditions)
 *  ii) A new thread is created
 *  iii) The new thread evaluates the function using the previously calculated arguments
 *
 * @author pcingola
 */
public class FunctionCallThread extends BdsThread {

	private static final long serialVersionUID = -3177132243907469975L;

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

	public void setFunctionCall(FunctionCall functionCall) {
		this.functionCall = functionCall;
	}

}
