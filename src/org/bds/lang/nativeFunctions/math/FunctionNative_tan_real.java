package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_tan_real extends FunctionNative {

	private static final long serialVersionUID = 7398988529272913920L;

	public FunctionNative_tan_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "tan";
		returnType = Types.REAL;

		String argNames[] = { "a" };
		Type argTypes[] = { Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.tan(bdsThread.getReal("a"));
	}
}
