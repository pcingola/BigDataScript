package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

/**
 * Native function "print"
 * 
 * @author pcingola
 */
public class FunctionNativePrint extends FunctionNative {

	public FunctionNativePrint() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "print";
		returnType = TypeList.get(Types.STRING);

		String argNames[] = { "str" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		String str = csThread.getString("str");
		System.out.print(str);
		return str;
	}

}
