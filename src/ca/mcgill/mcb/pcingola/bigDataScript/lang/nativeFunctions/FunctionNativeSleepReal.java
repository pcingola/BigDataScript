package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

/**
 * Native function "sleep"
 *
 * @author pcingola
 */
public class FunctionNativeSleepReal extends FunctionNative {

	public FunctionNativeSleepReal() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "sleep";
		returnType = TypeList.get(Type.BOOL);

		String argNames[] = { "seconds" };
		Type argTypes[] = { Type.REAL };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BdsThread csThread) {
		double secs = csThread.getReal("seconds");
		if (secs <= 0) return false;
		try {
			Thread.sleep((long) (secs * 1000));
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

}
