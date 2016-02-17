package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.run.BdsThread;

/**
 * Native function "rand". Return a random int number
 * 
 * @author pcingola
 */
public class FunctionNativeMinReal extends FunctionNative {

	public FunctionNativeMinReal() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "min";
		returnType = Type.REAL;

		String argNames[] = { "n1", "n2" };
		Type argTypes[] = { returnType, returnType };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		double n1 = csThread.getInt("n1");
		double n2 = csThread.getInt("n2");
		return Math.max(n1, n2);
	}
}
