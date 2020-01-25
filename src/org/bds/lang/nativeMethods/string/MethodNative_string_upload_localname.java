package org.bds.lang.nativeMethods.string;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_upload_localname extends MethodNativeString {

	private static final long serialVersionUID = 8938849860984012800L;

	public MethodNative_string_upload_localname() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "upload";
		classType = Types.STRING;
		returnType = Types.BOOL;

		String argNames[] = { "this", "localName" };
		Type argTypes[] = { Types.STRING, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String localName = bdsThread.getString("localName");
		Data data = bdsThread.data(objThis.toString());
		Data localData = bdsThread.data(localName);
		return data.upload(localData);
	}
}
