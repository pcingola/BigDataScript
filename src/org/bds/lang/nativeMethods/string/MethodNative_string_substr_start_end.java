package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_substr_start_end extends MethodNativeString {

	private static final long serialVersionUID = 8035928599414472704L;

	public MethodNative_string_substr_start_end() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "substr";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this", "start", "end" };
		Type argTypes[] = { Types.STRING, Types.INT, Types.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		String s = objThis.toString();
		int start = (int) csThread.getInt("start"), end = (int) csThread.getInt("end");
		start = Math.max(0, start);
		end = Math.min(end, s.length());
		return (start >= end ? "" : s.substring(start, end));
	}
}
