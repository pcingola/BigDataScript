package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNativeAssertString extends FunctionNativeAssert {

	private static final long serialVersionUID = 5159123581447208960L;


	public FunctionNativeAssertString() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "assert";
		returnType = Types.BOOL;

		String argNames[] = { "msg", "expected", "map" };
		Type argTypes[] = { Types.STRING, Types.STRING, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		String msg = bdsThread.getString("msg");
		String expected = bdsThread.getString("expected");
		String value = bdsThread.getString("map");
		if (!expected.equals(value)) //
			throw new RuntimeException("Expecting '" + expected + "', but was '" + value + "': " + msg);
		return true;
	}
}
