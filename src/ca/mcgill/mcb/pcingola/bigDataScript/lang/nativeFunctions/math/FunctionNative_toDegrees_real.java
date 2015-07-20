package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.math;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.FunctionNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

public class FunctionNative_toDegrees_real extends FunctionNative {
	public FunctionNative_toDegrees_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "toDegrees";
		returnType = Type.REAL;

		String argNames[] = { "angrad" };
		Type argTypes[] = { Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.toDegrees(bdsThread.getReal("angrad"));
	}
}
