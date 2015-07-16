package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.math;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.FunctionNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

public class FunctionNative_getExponent_real extends FunctionNative {
	public FunctionNative_getExponent_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "getExponent";
		returnType = Type.INT;

		String argNames[] = { "d" };
		Type argTypes[] = { Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BigDataScriptThread bdsThread) {
		return (long) Math.getExponent(bdsThread.getReal("d"));
	}
}
