package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.run.BdsThread;

public class FunctionNative_copySign_real_real extends FunctionNative {
	public FunctionNative_copySign_real_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "copySign";
		returnType = Type.REAL;

		String argNames[] = { "magnitude", "sign" };
		Type argTypes[] = { Type.REAL, Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.copySign(bdsThread.getReal("magnitude"), bdsThread.getReal("sign"));
	}
}
