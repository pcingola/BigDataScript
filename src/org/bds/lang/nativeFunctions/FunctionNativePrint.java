package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

/**
 * Native function "print"
 * 
 * @author pcingola
 */
public class FunctionNativePrint extends FunctionNative {

	private static final long serialVersionUID = 6415414552900435968L;

	public FunctionNativePrint() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "print";
		returnType = Types.STRING;

		String argNames[] = { "str" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		String str = bdsThread.getString("str");
		System.out.print(str);
		return str;
	}

}
