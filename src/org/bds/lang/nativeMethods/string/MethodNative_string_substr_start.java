package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_substr_start extends MethodNativeString {

	private static final long serialVersionUID = 5149994740128710656L;


	public MethodNative_string_substr_start() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "substr";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this", "start" };
		Type argTypes[] = { Types.STRING, Types.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String s = objThis.toString();
		int start = (int) bdsThread.getInt("start");
		start = Math.max(0, start);
		return s.substring(start);
	}
}
