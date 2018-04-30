package org.bds.lang.nativeFunctions;

import java.util.Date;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

/**
 * Native function "rand". Return a random int number
 *
 * @author pcingola
 */
public class FunctionNativeTime extends FunctionNative {

	private static final long serialVersionUID = 7645169541361729536L;


	public FunctionNativeTime() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "time";
		returnType = Types.INT;

		String argNames[] = {};
		Type argTypes[] = {};
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		return (new Date()).getTime();
	}
}
