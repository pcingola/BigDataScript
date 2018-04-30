package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_cos_real extends FunctionNative {

	private static final long serialVersionUID = 6508715295702548480L;

	public FunctionNative_cos_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "cos";
		returnType = Types.REAL;

		String argNames[] = { "a" };
		Type argTypes[] = { Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.cos(bdsThread.getReal("a"));
	}
}
