package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_clone_real extends FunctionNative {

	private static final long serialVersionUID = 8641329427621380096L;

	public FunctionNative_clone_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "clone";
		returnType = Types.REAL;

		String argNames[] = { "x" };
		Type argTypes[] = { Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return bdsThread.getReal("x");
	}
}
