package org.bigDataScript.lang.nativeFunctions;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;
import org.bigDataScript.run.BdsThread;
import org.bigDataScript.run.HelpCreator;

/**
 * Native function "sleep"
 *
 * @author pcingola
 */
public class FunctionNativePrintHelp extends FunctionNative {

	public FunctionNativePrintHelp() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "printHelp";
		returnType = TypeList.get(Type.BOOL);

		String argNames[] = {};
		Type argTypes[] = {};
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		HelpCreator hc = new HelpCreator(bdsThread.getRoot().getProgramUnit());
		System.out.println(hc);
		return true;
	}

}
