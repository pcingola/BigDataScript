package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

public class FunctionNativeAssertStringNoMsg extends FunctionNativeAssert {

	public FunctionNativeAssertStringNoMsg() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "assert";
		returnType = Type.BOOL;

		String argNames[] = { "expected", "value" };
		Type argTypes[] = { Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		String expected = bdsThread.getString("expected");
		String value = bdsThread.getString("value");
		if (!expected.equals(value)) //
			throw new RuntimeException("Expecting '" + expected + "', but was '" + value + "'");
		return true;
	}
}
