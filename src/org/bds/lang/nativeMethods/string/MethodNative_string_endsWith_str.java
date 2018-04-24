package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_endsWith_str extends MethodNativeString {

	private static final long serialVersionUID = 2437106683009335296L;

	public MethodNative_string_endsWith_str() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "endsWith";
		classType = Types.STRING;
		returnType = Types.BOOL;

		String argNames[] = { "this", "str" };
		Type argTypes[] = { Types.STRING, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return objThis.toString().endsWith(csThread.getString("str"));
	}
}
