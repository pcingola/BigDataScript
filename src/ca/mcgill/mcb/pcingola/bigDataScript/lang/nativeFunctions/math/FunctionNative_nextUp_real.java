package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.math;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.FunctionNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

public class FunctionNative_nextUp_real extends FunctionNative {
	public FunctionNative_nextUp_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "nextUp";
		returnType = Type.REAL;

		String argNames[] = { "d" };
		Type argTypes[] = { Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.nextUp(bdsThread.getReal("d"));
	}
}
