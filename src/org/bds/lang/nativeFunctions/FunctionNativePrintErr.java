package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

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
		returnType = TypeList.get(Types.STRING);

		String argNames[] = { "str" };
		Type argTypes[] = { Types.STRING };
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
