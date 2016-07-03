package org.bds.lang.nativeMethods.string;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.run.BdsThread;

public class MethodNative_string_baseName_ext extends MethodNative {
	public MethodNative_string_baseName_ext() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "baseName";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this", "ext" };
		Type argTypes[] = { Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		// Get basename
		String filePath = objThis.toString();
		if (filePath.isEmpty()) return "";
		Data data = bdsThread.data(filePath);
		String baseName = data.getName();

		// Remove ext
		String ext = bdsThread.getString("ext");
		if (baseName.endsWith(ext)) return baseName.substring(0, baseName.length() - ext.length());
		return baseName;
	}
}
