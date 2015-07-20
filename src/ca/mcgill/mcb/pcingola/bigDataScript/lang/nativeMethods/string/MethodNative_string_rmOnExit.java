package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

public class MethodNative_string_rmOnExit extends MethodNative {
	public MethodNative_string_rmOnExit() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "rmOnExit";
		classType = Type.STRING;
		returnType = Type.VOID;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		bdsThread.rmOnExit(objThis.toString());
		return objThis;
	}
}
