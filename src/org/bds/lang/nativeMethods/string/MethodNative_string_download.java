package org.bds.lang.nativeMethods.string;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_download extends MethodNativeString {

	private static final long serialVersionUID = 8554619589160370176L;

	public MethodNative_string_download() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "download";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		Data data = bdsThread.data(objThis.toString());
		if (!data.download()) return "";
		return data.getLocalPath();
	}
}
