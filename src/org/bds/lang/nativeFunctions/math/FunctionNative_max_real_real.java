package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

public class FunctionNative_max_real_real extends FunctionNative {
	public FunctionNative_max_real_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "max";
		returnType = Type.REAL;

		String argNames[] = { "a", "b" };
		Type argTypes[] = { Type.REAL, Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.max(bdsThread.getReal("a"), bdsThread.getReal("b"));
	}
}
