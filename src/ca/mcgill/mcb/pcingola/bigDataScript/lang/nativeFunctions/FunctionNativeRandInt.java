package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Native function "rand". Return a random int number
 * 
 * @author pcingola
 */
public class FunctionNativeRandInt extends FunctionNative {

	public FunctionNativeRandInt() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "randInt";
		returnType = Type.INT;

		String argNames[] = {};
		Type argTypes[] = {};
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BigDataScriptThread csThread) {
		return csThread.getRandom().nextLong();
	}
}
