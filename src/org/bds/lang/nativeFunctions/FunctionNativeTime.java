package org.bds.lang.nativeFunctions;

import java.util.Date;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

/**
 * Native function "rand". Return a random int number
 *
 * @author pcingola
 */
public class FunctionNativeTime extends FunctionNative {

	public FunctionNativeTime() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "time";
		returnType = Type.INT;

		String argNames[] = {};
		Type argTypes[] = {};
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		return (new Date()).getTime();
	}
}
