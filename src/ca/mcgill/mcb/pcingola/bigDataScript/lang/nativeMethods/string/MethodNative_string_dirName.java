package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

public class MethodNative_string_dirName extends MethodNative {
	public MethodNative_string_dirName() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "dirName";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BigDataScriptThread bdsThread, Object objThis) {
		try {
			String d = (bdsThread.file(objThis.toString())).getParent();
			return d != null ? d : ".";
		} catch (Exception e) {
			return "";
		}
	}
}
