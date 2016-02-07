package org.bigDataScript.lang.nativeMethods.string;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeMethods.MethodNative;
import org.bigDataScript.run.BdsThread;

public class MethodNative_string_dirName extends MethodNative {
	public MethodNative_string_dirName() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "dirName";
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
			String d = (bdsThread.data(objThis.toString())).getParent();
			return d != null ? d : ".";
		} catch (Exception e) {
			return "";
		}
	}
}
