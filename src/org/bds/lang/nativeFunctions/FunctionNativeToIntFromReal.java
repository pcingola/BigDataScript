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
public class FunctionNativeToIntFromReal extends FunctionNative {

	private static final long serialVersionUID = 1249719481043812352L;


	public FunctionNativeToIntFromReal() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "toInt";
		returnType = Types.INT;

		String argNames[] = { "num" };
		Type argTypes[] = { Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		double num = csThread.getReal("num");
		return ((long) num);
	}
}
