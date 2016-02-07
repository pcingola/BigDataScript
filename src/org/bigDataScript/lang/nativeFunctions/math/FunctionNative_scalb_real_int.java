package org.bigDataScript.lang.nativeFunctions.math;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeFunctions.FunctionNative;
import org.bigDataScript.run.BdsThread;

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
