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
public class FunctionNativeMaxInt extends FunctionNative {

	private static final long serialVersionUID = 5764361782864936960L;


	public FunctionNativeMaxInt() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "max";
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
		return Math.max(n1, n2);
	}
}
