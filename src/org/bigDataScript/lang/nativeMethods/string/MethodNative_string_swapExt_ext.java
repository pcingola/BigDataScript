package org.bigDataScript.lang.nativeMethods.string;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeMethods.MethodNative;
import org.bigDataScript.run.BdsThread;

public class MethodNative_string_swapExt_ext extends MethodNative {
	public MethodNative_string_swapExt_ext() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "swapExt";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this", "ext" };
		Type argTypes[] = { Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		String ext = csThread.getString("ext"); String b = objThis.toString(); if (b.endsWith(ext)) return b.substring(0, b.length() - ext.length()); return b;
	}
}
