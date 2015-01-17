package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

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
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		return objThis.toString().replace(csThread.getString("str1"), csThread.getString("str2"));
	}
}
