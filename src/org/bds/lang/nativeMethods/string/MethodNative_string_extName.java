package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_extName extends MethodNativeString {

	private static final long serialVersionUID = 880193074855182336L;

	public MethodNative_string_extName() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "extName";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String base = objThis.toString();
		int idx = base.lastIndexOf('.');
		return idx >= 0 ? base.substring(idx + 1) : "";
	}
}
