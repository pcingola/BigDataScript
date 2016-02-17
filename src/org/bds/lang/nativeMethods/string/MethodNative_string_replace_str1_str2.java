package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.run.BdsThread;

public class MethodNative_string_replace_str1_str2 extends MethodNative {
	public MethodNative_string_replace_str1_str2() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "replace";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this", "str1", "str2" };
		Type argTypes[] = { Type.STRING, Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return objThis.toString().replace(csThread.getString("str1"), csThread.getString("str2"));
	}
}
