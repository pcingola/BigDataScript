package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Native function "rand". Return a random number [0,1] interval
 * 
 * @author pcingola
 */
public class FunctionNativeRand extends FunctionNative {

	public FunctionNativeRand() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "rand";
		returnType = Type.REAL;

		String argNames[] = {};
		Type argTypes[] = {};
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BigDataScriptThread csThread) {
		return csThread.getRandom().nextDouble();
	}
}
