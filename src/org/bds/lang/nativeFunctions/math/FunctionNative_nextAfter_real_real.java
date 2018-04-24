package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_nextAfter_real_real extends FunctionNative {

	private static final long serialVersionUID = 1299233161112223744L;

	public FunctionNative_nextAfter_real_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "nextAfter";
		returnType = Types.REAL;

		String argNames[] = { "start", "direction" };
		Type argTypes[] = { Types.REAL, Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.nextAfter(bdsThread.getReal("start"), bdsThread.getReal("direction"));
	}
}
