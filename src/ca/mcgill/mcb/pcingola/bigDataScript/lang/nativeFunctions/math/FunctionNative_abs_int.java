package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.math;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.FunctionNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

public class FunctionNative_abs_int extends FunctionNative {
	public FunctionNative_abs_int() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "abs";
		returnType = Type.INT;

		String argNames[] = { "x" };
		Type argTypes[] = { Type.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Long) Math.abs(bdsThread.getInt("x"));
	}
}
