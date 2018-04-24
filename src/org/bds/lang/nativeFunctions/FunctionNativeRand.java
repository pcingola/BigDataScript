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
public class FunctionNativeRand extends FunctionNative {

	private static final long serialVersionUID = 524576886622289920L;


	public FunctionNativeRand() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "rand";
		returnType = Types.REAL;

		String argNames[] = {};
		Type argTypes[] = {};
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		return csThread.getRandom().nextDouble();
	}
}
