package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

/**
 * Native function "rand". Return a random int number
 * 
 * @author pcingola
 */
public class FunctionNativeToIntFromReal extends FunctionNative {

	public FunctionNativeToIntFromReal() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "toInt";
		returnType = Type.INT;

		String argNames[] = { "num" };
		Type argTypes[] = { Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		double num = csThread.getReal("num");
		return ((long) num);
	}
}
