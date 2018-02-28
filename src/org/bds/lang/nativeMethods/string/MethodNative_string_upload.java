package org.bds.lang.nativeMethods.string;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

public class MethodNative_string_upload extends MethodNative {

	public MethodNative_string_upload() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "upload";
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
		return data.upload();
	}
}
