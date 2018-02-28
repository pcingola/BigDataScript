package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

/**
 * Native function "rand". Return a random int number
 * 
 * @author pcingola
 */
public class FunctionNativeToIntFromBool extends FunctionNative {

	public FunctionNativeToIntFromBool() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "toInt";
		returnType = Type.INT;

		String argNames[] = { "num" };
		Type argTypes[] = { Type.BOOL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		boolean num = csThread.getBool("num");
		return (long) (num ? 1 : 0);
	}
}
