package org.bds.lang.nativeMethods.string;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.run.BdsThread;

public class MethodNative_string_baseName extends MethodNative {
	public MethodNative_string_baseName() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "baseName";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String filePath = objThis.toString();
		if (filePath.isEmpty()) return "";
		Data data = bdsThread.data(filePath);
		return data.getName();
	}
}
