package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.math;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.FunctionNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

public class FunctionNative_sin_real extends FunctionNative {
	public FunctionNative_sin_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "sin";
		returnType = Type.REAL;

		String argNames[] = { "a" };
		Type argTypes[] = { Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.sin(bdsThread.getReal("a"));
	}
}
