package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.math;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.FunctionNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

public class FunctionNative_scalb_real_int extends FunctionNative {
	public FunctionNative_scalb_real_int() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "scalb";
		returnType = Type.REAL;

		String argNames[] = { "d", "scaleFactor" };
		Type argTypes[] = { Type.REAL, Type.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BigDataScriptThread bdsThread) {
		return (Double) Math.scalb(bdsThread.getReal("d"), (int) bdsThread.getInt("scaleFactor"));
	}
}
