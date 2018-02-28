package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

public class FunctionNative_scalb_real_int extends FunctionNative {
	public FunctionNative_scalb_real_int() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "scalb";
		returnType = Type.REAL;

		String argNames[] = { "d", "scaleFactor" };
		Type argTypes[] = { Type.REAL, Type.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.scalb(bdsThread.getReal("d"), (int) bdsThread.getInt("scaleFactor"));
	}
}
