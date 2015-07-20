package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.math;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.FunctionNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

public class FunctionNative_copySign_real_real extends FunctionNative {
	public FunctionNative_copySign_real_real() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "copySign";
		returnType = Type.REAL;

		String argNames[] = { "magnitude", "sign" };
		Type argTypes[] = { Type.REAL, Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		return (Double) Math.copySign(bdsThread.getReal("magnitude"), bdsThread.getReal("sign"));
	}
}
