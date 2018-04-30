package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_atan_real extends FunctionNative {

	private static final long serialVersionUID = 3914771730018631680L;

	public FunctionNative_atan_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "atan";
		returnType = Types.REAL;

		String argNames[] = { "a" };
		Type argTypes[] = { Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.atan(bdsThread.getReal("a"));
	}
}
