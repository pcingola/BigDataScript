package org.bigDataScript.lang.nativeFunctions;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;
import org.bigDataScript.run.BdsThread;

/**
 * Native function "print" to STDERR
 * 
 * @author pcingola
 */
public class FunctionNativePrintErr extends FunctionNative {

	public FunctionNativePrintErr() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "printErr";
		returnType = TypeList.get(Type.STRING);

		String argNames[] = { "str" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		String str = csThread.getString("str");
		System.err.print(str);
		return str;
	}

}
