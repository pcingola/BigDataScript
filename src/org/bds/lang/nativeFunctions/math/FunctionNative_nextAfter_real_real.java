package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

public class FunctionNative_nextAfter_real_real extends FunctionNative {
	public FunctionNative_nextAfter_real_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "nextAfter";
		returnType = Type.REAL;

		String argNames[] = { "start", "direction" };
		Type argTypes[] = { Type.REAL, Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.nextAfter(bdsThread.getReal("start"), bdsThread.getReal("direction"));
	}
}
