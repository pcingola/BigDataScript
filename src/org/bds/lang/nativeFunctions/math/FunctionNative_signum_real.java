package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.run.BdsThread;

public class FunctionNative_signum_real extends FunctionNative {
	public FunctionNative_signum_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "signum";
		returnType = Type.REAL;

		String argNames[] = { "d" };
		Type argTypes[] = { Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.signum(bdsThread.getReal("d"));
	}
}
