package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_startsWith_str extends MethodNativeString {

	private static final long serialVersionUID = 2575450768626712576L;

	public MethodNative_string_startsWith_str() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "startsWith";
		classType = Types.STRING;
		returnType = Types.BOOL;

		String argNames[] = { "this", "str" };
		Type argTypes[] = { Types.STRING, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return objThis.toString().startsWith(csThread.getString("str"));
	}
}
