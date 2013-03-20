package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Native function "sleep"
 * 
 * @author pcingola
 */
public class FunctionNativeSleep extends FunctionNative {

	public FunctionNativeSleep() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "sleep";
		returnType = TypeList.get(Type.BOOL);

		String argNames[] = { "seconds" };
		Type argTypes[] = { Type.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@Override
	protected Object runFunctionNative(BigDataScriptThread csThread) {
		long secs = csThread.getInt("seconds");
		if (secs <= 0) return false;
		try {
			Thread.sleep(secs * 1000);
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

}
