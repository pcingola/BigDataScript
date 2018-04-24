package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_scalb_real_int extends FunctionNative {

	private static final long serialVersionUID = 528414974624694272L;

	public FunctionNative_scalb_real_int() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "scalb";
		returnType = Types.REAL;

		String argNames[] = { "d", "scaleFactor" };
		Type argTypes[] = { Types.REAL, Types.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.scalb(bdsThread.getReal("d"), (int) bdsThread.getInt("scaleFactor"));
	}
}
