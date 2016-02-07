package org.bigDataScript.lang.nativeFunctions;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;
import org.bigDataScript.run.BdsThread;
import org.bigDataScript.util.Timer;

/**
 * Native function "print" to STDERR
 *
 * @author pcingola
 */
public class FunctionNativeLog extends FunctionNative {

	public FunctionNativeLog() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "log";
		returnType = TypeList.get(Type.STRING);

		String argNames[] = { "str" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		String str = csThread.getString("str");
		Timer.showStdErr(str);
		return str;
	}

}
