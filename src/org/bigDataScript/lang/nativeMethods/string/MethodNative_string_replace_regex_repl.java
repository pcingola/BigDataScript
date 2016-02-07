package org.bigDataScript.lang.nativeMethods.string;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeMethods.MethodNative;
import org.bigDataScript.run.BdsThread;

public class MethodNative_string_replace_regex_repl extends MethodNative {
	public MethodNative_string_replace_regex_repl() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "replaceAll";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this", "regex", "repl" };
		Type argTypes[] = { Type.STRING, Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return objThis.toString().replaceAll(csThread.getString("regex"), csThread.getString("repl"));
	}
}
