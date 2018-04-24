package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_cosh_real extends FunctionNative {

	private static final long serialVersionUID = 935525119202197504L;

	public FunctionNative_cosh_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "cosh";
		returnType = Types.REAL;

		String argNames[] = { "x" };
		Type argTypes[] = { Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.cosh(bdsThread.getReal("x"));
	}
}
