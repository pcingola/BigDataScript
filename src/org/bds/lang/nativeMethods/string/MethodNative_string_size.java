package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_size extends MethodNativeString {

	private static final long serialVersionUID = 5513080956673622016L;

	public MethodNative_string_size() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "size";
		classType = Types.STRING;
		returnType = Types.INT;

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		return (long) (bdsThread.data(objThis.toString())).size();
	}
}
