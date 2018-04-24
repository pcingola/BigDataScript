package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_tanh_real extends FunctionNative {

	private static final long serialVersionUID = 2972204570232520704L;

	public FunctionNative_tanh_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "tanh";
		returnType = Types.REAL;

		String argNames[] = { "x" };
		Type argTypes[] = { Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.tanh(bdsThread.getReal("x"));
	}
}
