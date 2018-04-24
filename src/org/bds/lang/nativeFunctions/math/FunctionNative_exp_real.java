package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_exp_real extends FunctionNative {

	private static final long serialVersionUID = 678097430306127872L;

	public FunctionNative_exp_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "exp";
		returnType = Types.REAL;

		String argNames[] = { "a" };
		Type argTypes[] = { Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.exp(bdsThread.getReal("a"));
	}
}
