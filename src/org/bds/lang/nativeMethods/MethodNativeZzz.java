package org.bds.lang.nativeMethods;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

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
