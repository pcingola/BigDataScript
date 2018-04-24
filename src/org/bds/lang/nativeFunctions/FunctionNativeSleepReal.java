package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

/**
 * Native function "sleep"
 *
 * @author pcingola
 */
public class FunctionNativeSleepReal extends FunctionNative {

	private static final long serialVersionUID = 8504357687134289920L;


	public FunctionNativeSleepReal() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "sleep";
		returnType = Types.BOOL;

		String argNames[] = { "seconds" };
		Type argTypes[] = { Types.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		double secs = bdsThread.getReal("seconds");
		if (secs <= 0) return false;
		try {
			Thread.sleep((long) (secs * 1000));
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

}
