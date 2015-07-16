package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.math;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.FunctionNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

public class FunctionNative_IEEEremainder_real_real extends FunctionNative {
	public FunctionNative_IEEEremainder_real_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "IEEEremainder";
		returnType = Type.REAL;

		String argNames[] = { "f1", "f2" };
		Type argTypes[] = { Type.REAL, Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BigDataScriptThread bdsThread) {
		return (Double) Math.IEEEremainder(bdsThread.getReal("f1"), bdsThread.getReal("f2"));
	}
}
