package org.bds.lang.nativeMethods;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
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
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this", "ext" };
		Type argTypes[] = { classType, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return objThis.toString();
	}
}
