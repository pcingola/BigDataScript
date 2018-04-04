package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

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
		returnType = Types.INT;

		String argNames[] = { "range" };
		Type argTypes[] = { Types.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		long range = csThread.getInt("range");
		return Math.abs(csThread.getRandom().nextLong() % range);
	}
}
