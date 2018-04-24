package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_IEEEremainder_real_real extends FunctionNative {

	private static final long serialVersionUID = 4155394957789659136L;

	public FunctionNative_IEEEremainder_real_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "IEEEremainder";
		returnType = Types.REAL;

		String argNames[] = { "f1", "f2" };
		Type argTypes[] = { Types.REAL, Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.IEEEremainder(bdsThread.getReal("f1"), bdsThread.getReal("f2"));
	}
}
