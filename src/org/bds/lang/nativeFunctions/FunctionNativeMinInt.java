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
public class FunctionNativeMinInt extends FunctionNative {

	private static final long serialVersionUID = 5831982046473388032L;


	public FunctionNativeMinInt() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "min";
		returnType = Types.INT;

		String argNames[] = { "n1", "n2" };
		Type argTypes[] = { returnType, returnType };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		long n1 = csThread.getInt("n1");
		long n2 = csThread.getInt("n2");
		return Math.min(n1, n2);
	}
}
