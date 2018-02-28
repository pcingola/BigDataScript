package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

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
		returnType = TypeList.get(Types.BOOL);

		String argNames[] = { "seconds" };
		Type argTypes[] = { Types.INT };
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
