package org.bigDataScript.lang.nativeMethods.string;

import org.bigDataScript.data.Data;
import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeMethods.MethodNative;
import org.bigDataScript.run.BdsThread;

public class MethodNative_string_download extends MethodNative {
	public MethodNative_string_download() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "download";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		Data data = bdsThread.data(objThis.toString());
		if (!data.download()) return "";
		return data.getLocalPath();
	}
}
