package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNative_clone_string extends FunctionNative {

	private static final long serialVersionUID = 6091181325627195392L;

	public FunctionNative_clone_string() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "clone";
		returnType = Types.STRING;

		String argNames[] = { "x" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return bdsThread.getString("x");
	}
}
