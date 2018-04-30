package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_swapExt_extNew extends MethodNativeString {

	private static final long serialVersionUID = 7904780671372001280L;

	public MethodNative_string_swapExt_extNew() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "swapExt";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this", "extNew" };
		Type argTypes[] = { Types.STRING, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		String base = objThis.toString();
		String extNew = csThread.getString("extNew");
		int idx = base.lastIndexOf('.');
		return idx >= 0 ? base.substring(0, idx) + "." + extNew : base + "." + extNew;
	}
}
