package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_lastIndexOf_str extends MethodNativeString {

	private static final long serialVersionUID = 1407046042209255424L;

	public MethodNative_string_lastIndexOf_str() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "lastIndexOf";
		classType = Types.STRING;
		returnType = Types.INT;

		String argNames[] = { "this", "str" };
		Type argTypes[] = { Types.STRING, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return (long) objThis.toString().lastIndexOf(csThread.getString("str"));
	}
}
