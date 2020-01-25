package org.bds.lang.nativeMethods.string;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_download_localname extends MethodNativeString {

	private static final long serialVersionUID = 7065502228752138240L;

	public MethodNative_string_download_localname() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "download";
		classType = Types.STRING;
		returnType = Types.STRING;

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
		return data.download(localData);
	}
}
