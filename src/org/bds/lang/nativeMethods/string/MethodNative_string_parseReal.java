package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.util.Gpr;

public class MethodNative_string_parseReal extends MethodNativeString {

	private static final long serialVersionUID = 4978246041708036096L;

	public MethodNative_string_parseReal() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "parseReal";
		classType = Types.STRING;
		returnType = Types.REAL;

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return Gpr.parseDoubleSafe(objThis.toString().trim());
	}
}
