package org.bigDataScript.lang.nativeFunctions.math;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeFunctions.FunctionNative;
import org.bigDataScript.run.BdsThread;

public class FunctionNative_abs_int extends FunctionNative {
	public FunctionNative_abs_int() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "abs";
		returnType = Type.INT;

		String argNames[] = { "x" };
		Type argTypes[] = { Type.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Long) Math.abs(bdsThread.getInt("x"));
	}
}
