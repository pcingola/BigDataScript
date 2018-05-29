package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.run.HelpCreator;

/**
 * Native function "sleep"
 *
 * @author pcingola
 */
public class FunctionNativePrintHelp extends FunctionNative {

	private static final long serialVersionUID = 6623072869440061440L;

	public FunctionNativePrintHelp() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "printHelp";
		returnType = Types.BOOL;

		String argNames[] = {};
		Type argTypes[] = {};
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		HelpCreator hc = new HelpCreator(bdsThread.getRoot().getProgramUnit());
		System.out.println(hc);
		return true;
	}

}
