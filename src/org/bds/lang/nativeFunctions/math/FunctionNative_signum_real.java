package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_signum_real extends FunctionNative {

	private static final long serialVersionUID = 7598253174197344883L;

	public FunctionNative_signum_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "signum";
		returnType = Types.REAL;

		String argNames[] = { "d" };
		Type argTypes[] = { Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return Math.signum(bdsThread.getReal("d"));
	}
}
