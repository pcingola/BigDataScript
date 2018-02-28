package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

public class MethodNative_string_canExec extends MethodNative {
	public MethodNative_string_canExec() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "canExec";
		classType = Type.STRING;
		returnType = Type.BOOL;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		return (bdsThread.data(objThis.toString())).canExecute();
	}
}
