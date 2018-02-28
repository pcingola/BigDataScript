package org.bds.lang.nativeMethods.string;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

public class MethodNative_string_download_localname extends MethodNative {
	public MethodNative_string_download_localname() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "download";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this", "localName" };
		Type argTypes[] = { Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String localName = bdsThread.getString("localName");

		Data data = bdsThread.data(objThis.toString());
		Data localData = bdsThread.data(localName);
		return data.download(localData.getAbsolutePath());
	}
}
