package org.bigDataScript.lang.nativeFunctions.math;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeFunctions.FunctionNative;
import org.bigDataScript.run.BdsThread;

public class FunctionNative_toDegrees_real extends FunctionNative {
	public FunctionNative_toDegrees_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "toDegrees";
		returnType = Type.REAL;

		String argNames[] = { "angrad" };
		Type argTypes[] = { Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.toDegrees(bdsThread.getReal("angrad"));
	}
}
