package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

public class MethodNative_string_swapExt_extNew extends MethodNative {
	public MethodNative_string_swapExt_extNew() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "swapExt";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this", "extNew" };
		Type argTypes[] = { Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		String base = objThis.toString(); String extNew = csThread.getString("extNew"); int idx = base.lastIndexOf('.'); return idx >= 0 ? base.substring(0, idx) + "." + extNew : base + "." + extNew;
	}
}
