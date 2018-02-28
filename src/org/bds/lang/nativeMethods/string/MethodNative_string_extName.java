package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

public class MethodNative_string_extName extends MethodNative {
	public MethodNative_string_extName() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "extName";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String base = objThis.toString();
		int idx = base.lastIndexOf('.');
		return idx >= 0 ? base.substring(idx + 1) : "";
	}
}
