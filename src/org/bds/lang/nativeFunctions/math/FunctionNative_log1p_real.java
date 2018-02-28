package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

public class FunctionNative_log1p_real extends FunctionNative {
	public FunctionNative_log1p_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "log1p";
		returnType = Type.REAL;

		String argNames[] = { "x" };
		Type argTypes[] = { Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.log1p(bdsThread.getReal("x"));
	}
}
