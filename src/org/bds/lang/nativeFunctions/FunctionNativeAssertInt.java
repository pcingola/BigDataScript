package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNativeAssertInt extends FunctionNativeAssert {

	public FunctionNativeAssertInt() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "assert";
		returnType = Types.BOOL;

		String argNames[] = { "msg", "expected", "value" };
		Type argTypes[] = { Types.STRING, Types.INT, Types.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		String msg = bdsThread.getString("msg");
		long expected = bdsThread.getInt("expected");
		long value = bdsThread.getInt("value");
		if (expected != value) //
			throw new RuntimeException("Expecting '" + expected + "', but was '" + value + "': " + msg);
		return true;
	}
}
