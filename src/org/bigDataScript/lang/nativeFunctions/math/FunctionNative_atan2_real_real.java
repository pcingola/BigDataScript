package org.bigDataScript.lang.nativeFunctions.math;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeFunctions.FunctionNative;
import org.bigDataScript.run.BdsThread;

public class FunctionNative_atan2_real_real extends FunctionNative {
	public FunctionNative_atan2_real_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "atan2";
		returnType = Type.REAL;

		String argNames[] = { "y", "x" };
		Type argTypes[] = { Type.REAL, Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.atan2(bdsThread.getReal("y"), bdsThread.getReal("x"));
	}
}
