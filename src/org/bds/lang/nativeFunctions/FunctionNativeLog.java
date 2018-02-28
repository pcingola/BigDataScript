package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.run.BdsThread;
import org.bds.util.Timer;

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
