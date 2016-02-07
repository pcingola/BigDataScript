package org.bigDataScript.lang.nativeFunctions.math;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeFunctions.FunctionNative;
import org.bigDataScript.run.BdsThread;

public class FunctionNative_toRadians_real extends FunctionNative {
	public FunctionNative_toRadians_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "toRadians";
		returnType = Type.REAL;

		String argNames[] = { "angdeg" };
		Type argTypes[] = { Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.toRadians(bdsThread.getReal("angdeg"));
	}
}
