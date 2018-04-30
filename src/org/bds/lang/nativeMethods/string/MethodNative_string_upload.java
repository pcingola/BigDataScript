package org.bds.lang.nativeMethods.string;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_upload extends MethodNativeString {

	private static final long serialVersionUID = 376835702471557120L;


	public MethodNative_string_upload() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "upload";
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
		return data.upload();
	}
}
