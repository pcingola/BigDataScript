package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_cbrt_real extends FunctionNative {

	private static final long serialVersionUID = 1359538287961735168L;

	public FunctionNative_cbrt_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "cbrt";
		returnType = Types.REAL;

		String argNames[] = { "a" };
		Type argTypes[] = { Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.cbrt(bdsThread.getReal("a"));
	}
}
