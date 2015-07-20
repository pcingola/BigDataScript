package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.HelpCreator;

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
