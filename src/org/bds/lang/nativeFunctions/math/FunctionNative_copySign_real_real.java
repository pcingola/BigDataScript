package org.bds.lang.nativeFunctions.math;

import org.bds.lang.Parameters;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_copySign_real_real extends FunctionNative {

	private static final long serialVersionUID = 7852734146009923584L;

	public FunctionNative_copySign_real_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "copySign";
		returnType = Types.REAL;

		String argNames[] = { "magnitude", "sign" };
		Type argTypes[] = { Types.REAL, Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.copySign(bdsThread.getReal("magnitude"), bdsThread.getReal("sign"));
	}
}
