package org.bigDataScript.lang.nativeFunctions;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.run.BdsThread;

/**
 * Native function "rand". Return a random int number
 * 
 * @author pcingola
 */
public class FunctionNativeMinInt extends FunctionNative {

	public FunctionNativeMinInt() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "min";
		returnType = Type.INT;

		String argNames[] = { "n1", "n2" };
		Type argTypes[] = { returnType, returnType };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		long n1 = csThread.getInt("n1");
		long n2 = csThread.getInt("n2");
		return Math.min(n1, n2);
	}
}
