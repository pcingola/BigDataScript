package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

public class FunctionNativeAssertBoolNoMsg extends FunctionNativeAssert {

	public FunctionNativeAssertBoolNoMsg() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "assert";
		returnType = Type.BOOL;

		String argNames[] = { "cond" };
		Type argTypes[] = { Type.BOOL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		boolean cond = bdsThread.getBool("cond");
		if (!cond) throw new RuntimeException("Assertion failed.");
		return true;
	}
}
