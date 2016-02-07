package org.bigDataScript.lang.nativeFunctions.math;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeFunctions.FunctionNative;
import org.bigDataScript.run.BdsThread;

public class FunctionNative_hypot_real_real extends FunctionNative {
	public FunctionNative_hypot_real_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "hypot";
		returnType = Type.REAL;

		String argNames[] = { "x", "y" };
		Type argTypes[] = { Type.REAL, Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.hypot(bdsThread.getReal("x"), bdsThread.getReal("y"));
	}
}
