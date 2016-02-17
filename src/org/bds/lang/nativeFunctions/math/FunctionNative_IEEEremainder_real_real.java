package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.run.BdsThread;

public class FunctionNative_IEEEremainder_real_real extends FunctionNative {
	public FunctionNative_IEEEremainder_real_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "IEEEremainder";
		returnType = Type.REAL;

		String argNames[] = { "f1", "f2" };
		Type argTypes[] = { Type.REAL, Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.IEEEremainder(bdsThread.getReal("f1"), bdsThread.getReal("f2"));
	}
}
