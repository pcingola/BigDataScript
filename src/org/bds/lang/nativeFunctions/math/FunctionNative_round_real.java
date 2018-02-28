package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

public class FunctionNative_round_real extends FunctionNative {
	public FunctionNative_round_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "round";
		returnType = Type.INT;

		String argNames[] = { "a" };
		Type argTypes[] = { Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Long) Math.round(bdsThread.getReal("a"));
	}
}
