package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

/**
 * Method for testing
 * 
 * @author pcingola
 */
public class MethodNativeZzz extends MethodNative {

	public MethodNativeZzz() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "zzz";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this", "ext" };
		Type argTypes[] = { classType, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return objThis.toString();
	}
}
