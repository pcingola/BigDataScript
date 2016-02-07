package org.bigDataScript.lang.nativeFunctions;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;
import org.bigDataScript.run.BdsThread;

/**
 * Native function "sleep"
 *
 * @author pcingola
 */
public class FunctionNativeSleep extends FunctionNative {

	public FunctionNativeSleep() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "sleep";
		returnType = TypeList.get(Type.BOOL);

		String argNames[] = { "seconds" };
		Type argTypes[] = { Type.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		long secs = csThread.getInt("seconds");
		if (secs <= 0) return false;
		try {
			Thread.sleep(secs * 1000);
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

}
