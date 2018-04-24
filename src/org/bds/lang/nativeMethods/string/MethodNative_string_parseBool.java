package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.util.Gpr;

public class MethodNative_string_parseBool extends MethodNativeString {

	private static final long serialVersionUID = 3221030229908684800L;

	public MethodNative_string_parseBool() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "parseBool";
		classType = Types.STRING;
		returnType = Types.BOOL;

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return Gpr.parseBoolSafe(objThis.toString().trim());
	}
}
