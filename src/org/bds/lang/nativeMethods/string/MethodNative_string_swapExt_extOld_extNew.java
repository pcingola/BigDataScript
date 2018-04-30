package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_swapExt_extOld_extNew extends MethodNativeString {

	private static final long serialVersionUID = 2815997079975919616L;

	public MethodNative_string_swapExt_extOld_extNew() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "swapExt";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this", "extOld", "extNew" };
		Type argTypes[] = { Types.STRING, Types.STRING, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		String extNew = csThread.getString("extNew");
		String extOld = csThread.getString("extOld");
		String b = objThis.toString();
		if (b.endsWith(extOld)) return b.substring(0, b.length() - extOld.length()) + extNew;
		return b + extNew;
	}
}
