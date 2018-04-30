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
public class FunctionNativeToIntFromBool extends FunctionNative {

	private static final long serialVersionUID = 5375641960251949056L;


	public FunctionNativeToIntFromBool() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "toInt";
		returnType = Types.INT;

		String argNames[] = { "num" };
		Type argTypes[] = { Types.BOOL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		boolean num = csThread.getBool("num");
		return (long) (num ? 1 : 0);
	}
}
