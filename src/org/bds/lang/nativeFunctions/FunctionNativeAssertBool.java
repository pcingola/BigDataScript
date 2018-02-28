package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNativeAssertBool extends FunctionNativeAssert {

	public FunctionNativeAssertBool() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "assert";
		returnType = Types.BOOL;

		String argNames[] = { "msg", "cond" };
		Type argTypes[] = { Types.STRING, Types.BOOL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		String msg = bdsThread.getString("msg");
		boolean cond = bdsThread.getBool("cond");
		if (!cond) throw new RuntimeException("Assertion failed: " + msg);
		return true;
	}
}
