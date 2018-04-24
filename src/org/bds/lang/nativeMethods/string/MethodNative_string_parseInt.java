package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.util.Gpr;

public class MethodNative_string_parseInt extends MethodNativeString {

	private static final long serialVersionUID = 4384952790733062144L;

	public MethodNative_string_parseInt() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "parseInt";
		classType = Types.STRING;
		returnType = Types.INT;

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return Gpr.parseLongSafe(objThis.toString().trim());
	}
}
