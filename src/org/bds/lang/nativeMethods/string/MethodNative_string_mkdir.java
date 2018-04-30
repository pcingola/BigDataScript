package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_mkdir extends MethodNativeString {

	private static final long serialVersionUID = 2289185086609850368L;

	public MethodNative_string_mkdir() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "mkdir";
		classType = Types.STRING;
		returnType = Types.BOOL;

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		return (bdsThread.data(objThis.toString())).mkdirs();
	}
}
