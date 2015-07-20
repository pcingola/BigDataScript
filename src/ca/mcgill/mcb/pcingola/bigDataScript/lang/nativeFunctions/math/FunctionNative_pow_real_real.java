package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.math;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.FunctionNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

public class FunctionNative_pow_real_real extends FunctionNative {
	public FunctionNative_pow_real_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "pow";
		returnType = Type.REAL;

		String argNames[] = { "a", "b" };
		Type argTypes[] = { Type.REAL, Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.pow(bdsThread.getReal("a"), bdsThread.getReal("b"));
	}
}
