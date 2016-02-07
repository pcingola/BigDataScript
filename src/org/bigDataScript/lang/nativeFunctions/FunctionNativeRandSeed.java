package org.bigDataScript.lang.nativeFunctions;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.run.BdsThread;

/**
 * Native function "rand". Return a random number [0,1] interval
 *
 * @author pcingola
 */
public class FunctionNativeRandSeed extends FunctionNative {

	public FunctionNativeRandSeed() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "randSeed";
		returnType = Type.VOID;

		String argNames[] = { "seed" };
		Type argTypes[] = { Type.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		long seed = csThread.getInt("seed");
		csThread.setRandomSeed(seed);
		return null;
	}
}
