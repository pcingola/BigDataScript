package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import java.util.Date;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

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
