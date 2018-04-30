package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_replace_str1_str2 extends MethodNativeString {

	private static final long serialVersionUID = 3693790038379757568L;

	public MethodNative_string_replace_str1_str2() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "replace";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this", "str1", "str2" };
		Type argTypes[] = { Types.STRING, Types.STRING, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return objThis.toString().replace(csThread.getString("str1"), csThread.getString("str2"));
	}
}
