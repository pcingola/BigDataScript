package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_replace_regex_repl extends MethodNativeString {

	private static final long serialVersionUID = 553760238063353856L;

	public MethodNative_string_replace_regex_repl() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "replaceAll";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this", "regex", "repl" };
		Type argTypes[] = { Types.STRING, Types.STRING, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return objThis.toString().replaceAll(csThread.getString("regex"), csThread.getString("repl"));
	}
}
