package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class FunctionNativeAssertBoolNoMsg extends FunctionNativeAssert {

	private static final long serialVersionUID = 1586200183371235328L;

	public FunctionNativeAssertBoolNoMsg() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "assert";
		returnType = Types.BOOL;

		String argNames[] = { "cond" };
		Type argTypes[] = { Types.BOOL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		boolean cond = bdsThread.getBool("cond");
		if (!cond) throw new RuntimeException("Assertion failed.");
		return true;
	}
}
