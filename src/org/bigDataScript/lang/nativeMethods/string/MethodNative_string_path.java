package org.bigDataScript.lang.nativeMethods.string;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeMethods.MethodNative;
import org.bigDataScript.run.BdsThread;

public class MethodNative_string_path extends MethodNative {
	public MethodNative_string_path() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "path";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		try {
			return (bdsThread.data(objThis.toString())).getAbsolutePath();
		} catch (Exception e) {
			return "";
		}
	}
}
