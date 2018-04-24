package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_rint_real extends FunctionNative {

	private static final long serialVersionUID = 6864390105944064000L;

	public FunctionNative_rint_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "rint";
		returnType = Types.REAL;

		String argNames[] = { "a" };
		Type argTypes[] = { Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.rint(bdsThread.getReal("a"));
	}
}
