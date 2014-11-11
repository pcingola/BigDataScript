package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

public class MethodNative_string_swapExt_extOld_extNew extends MethodNative {
	public MethodNative_string_swapExt_extOld_extNew() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "swapExt";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this", "extOld", "extNew" };
		Type argTypes[] = { Type.STRING, Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		String extNew = csThread.getString("extNew"); String extOld = csThread.getString("extOld"); String b = objThis.toString(); if (b.endsWith(extOld)) return b.substring(0, b.length() - extOld.length()) + extNew; return b + extNew;
	}
}
