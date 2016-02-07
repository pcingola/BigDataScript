package org.bigDataScript.lang.nativeFunctions;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.run.BdsThread;

/**
 * Native function "rand". Return a random number [0,1] interval
 * 
 * @author pcingola
 */
public class FunctionNativeRand extends FunctionNative {

	public FunctionNativeRand() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "rand";
		returnType = Type.REAL;

		String argNames[] = {};
		Type argTypes[] = {};
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		return csThread.getRandom().nextDouble();
	}
}
