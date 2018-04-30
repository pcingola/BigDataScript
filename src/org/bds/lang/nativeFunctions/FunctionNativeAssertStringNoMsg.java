package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNativeAssertStringNoMsg extends FunctionNativeAssert {

	private static final long serialVersionUID = 5665572123398144000L;


	public FunctionNativeAssertStringNoMsg() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "assert";
		returnType = Types.BOOL;

		String argNames[] = { "expected", "map" };
		Type argTypes[] = { Types.STRING, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		String expected = bdsThread.getString("expected");
		String value = bdsThread.getString("map");
		if (!expected.equals(value)) //
			throw new RuntimeException("Expecting '" + expected + "', but was '" + value + "'");
		return true;
	}
}
