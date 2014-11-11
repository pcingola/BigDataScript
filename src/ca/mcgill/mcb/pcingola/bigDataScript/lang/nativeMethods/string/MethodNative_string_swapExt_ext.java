package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

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
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		String ext = csThread.getString("ext"); String b = objThis.toString(); if (b.endsWith(ext)) return b.substring(0, b.length() - ext.length()); return b;
	}
}
