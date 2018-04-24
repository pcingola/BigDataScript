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
public class FunctionNativeRandInt extends FunctionNative {

	private static final long serialVersionUID = 5873732184459739136L;


	public FunctionNativeRandInt() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "randInt";
		returnType = Types.INT;

		String argNames[] = {};
		Type argTypes[] = {};
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		return csThread.getRandom().nextLong();
	}
}
