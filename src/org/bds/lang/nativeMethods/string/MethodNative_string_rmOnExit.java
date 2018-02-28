package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

public class MethodNative_string_rmOnExit extends MethodNative {
	public MethodNative_string_rmOnExit() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "rmOnExit";
		classType = Type.STRING;
		returnType = Type.VOID;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		bdsThread.rmOnExit(objThis.toString());
		return objThis;
	}
}
