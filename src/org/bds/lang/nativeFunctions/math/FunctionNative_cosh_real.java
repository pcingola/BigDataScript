package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.run.BdsThread;

public class FunctionNative_cosh_real extends FunctionNative {
	public FunctionNative_cosh_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "cosh";
		returnType = Type.REAL;

		String argNames[] = { "x" };
		Type argTypes[] = { Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.cosh(bdsThread.getReal("x"));
	}
}
