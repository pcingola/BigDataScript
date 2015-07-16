package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.math;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.FunctionNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

public class FunctionNative_atan2_real_real extends FunctionNative {
	public FunctionNative_atan2_real_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "atan2";
		returnType = Type.REAL;

		String argNames[] = { "y", "x" };
		Type argTypes[] = { Type.REAL, Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BigDataScriptThread bdsThread) {
		return (Double) Math.atan2(bdsThread.getReal("y"), bdsThread.getReal("x"));
	}
}
