package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_atan2_real_real extends FunctionNative {

	private static final long serialVersionUID = 2093004026429005112L;

	public FunctionNative_atan2_real_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "atan2";
		returnType = Types.REAL;

		String argNames[] = { "y", "x" };
		Type argTypes[] = { Types.REAL, Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return Math.atan2(bdsThread.getReal("y"), bdsThread.getReal("x"));
	}
}
