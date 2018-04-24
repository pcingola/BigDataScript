package org.bds.lang.nativeMethods.string;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_baseName_ext extends MethodNativeString {

	private static final long serialVersionUID = 708502319838298112L;


	public MethodNative_string_baseName_ext() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "baseName";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this", "ext" };
		Type argTypes[] = { Types.STRING, Types.STRING };
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
