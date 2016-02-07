package org.bigDataScript.lang.nativeFunctions.math;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeFunctions.FunctionNative;
import org.bigDataScript.run.BdsThread;

public class FunctionNative_expm1_real extends FunctionNative {
	public FunctionNative_expm1_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "expm1";
		returnType = Type.REAL;

		String argNames[] = { "x" };
		Type argTypes[] = { Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.expm1(bdsThread.getReal("x"));
	}
}
