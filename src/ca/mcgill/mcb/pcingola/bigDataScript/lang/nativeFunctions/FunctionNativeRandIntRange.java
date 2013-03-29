package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Native function "rand". Return a random int number
 * 
 * @author pcingola
 */
public class FunctionNativeRandIntRange extends FunctionNative {

	public FunctionNativeRandIntRange() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "randInt";
		returnType = Type.INT;

		String argNames[] = { "range" };
		Type argTypes[] = { Type.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BigDataScriptThread csThread) {
		long range = csThread.getInt("range");
		return Math.abs(csThread.getRandom().nextLong() % range);
	}
}
