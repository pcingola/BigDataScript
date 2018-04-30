package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_swapExt extends MethodNativeString {

	private static final long serialVersionUID = 6111834300787752960L;

	public MethodNative_string_swapExt() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "swapExt";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		String base = objThis.toString();
		int idx = base.lastIndexOf('.');
		return idx >= 0 ? base.substring(0, idx) : "";
	}
}
