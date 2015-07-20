package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

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
