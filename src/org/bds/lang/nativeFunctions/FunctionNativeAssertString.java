package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.run.BdsThread;

public class FunctionNativeAssertString extends FunctionNativeAssert {

	public FunctionNativeAssertString() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "assert";
		returnType = Type.BOOL;

		String argNames[] = { "msg", "expected", "value" };
		Type argTypes[] = { Type.STRING, Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		String msg = bdsThread.getString("msg");
		String expected = bdsThread.getString("expected");
		String value = bdsThread.getString("value");
		if (!expected.equals(value)) //
			throw new RuntimeException("Expecting '" + expected + "', but was '" + value + "': " + msg);
		return true;
	}
}
