package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

/**
 * Native function "rand". Return a random number [0,1] interval
 *
 * @author pcingola
 */
public class FunctionNativeRandSeed extends FunctionNative {

	private static final long serialVersionUID = 382788342460219392L;


	public FunctionNativeRandSeed() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "randSeed";
		returnType = Types.VOID;

		String argNames[] = { "seed" };
		Type argTypes[] = { Types.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		long seed = csThread.getInt("seed");
		csThread.setRandomSeed(seed);
		return null;
	}
}
