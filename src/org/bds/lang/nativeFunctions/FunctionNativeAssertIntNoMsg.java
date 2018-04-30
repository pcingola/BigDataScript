package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNativeAssertIntNoMsg extends FunctionNativeAssert {

	private static final long serialVersionUID = 4368103717782716416L;


	public FunctionNativeAssertIntNoMsg() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "assert";
		returnType = Types.BOOL;

		String argNames[] = { "expected", "map" };
		Type argTypes[] = { Types.INT, Types.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		long expected = bdsThread.getInt("expected");
		long value = bdsThread.getInt("map");
		if (expected != value) //
			throw new RuntimeException("Expecting '" + expected + "', but was '" + value + "'.");
		return true;
	}
}
