package org.bigDataScript.lang.nativeMethods.string;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeMethods.MethodNative;
import org.bigDataScript.run.BdsThread;

public class MethodNative_string_mkdir extends MethodNative {
	public MethodNative_string_mkdir() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "mkdir";
		classType = Type.STRING;
		returnType = Type.BOOL;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		return (bdsThread.data(objThis.toString())).mkdirs();
	}
}
