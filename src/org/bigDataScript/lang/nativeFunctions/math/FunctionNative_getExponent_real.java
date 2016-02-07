package org.bigDataScript.lang.nativeFunctions.math;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeFunctions.FunctionNative;
import org.bigDataScript.run.BdsThread;

public class FunctionNative_getExponent_real extends FunctionNative {
	public FunctionNative_getExponent_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "getExponent";
		returnType = Type.INT;

		String argNames[] = { "d" };
		Type argTypes[] = { Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (long) Math.getExponent(bdsThread.getReal("d"));
	}
}
