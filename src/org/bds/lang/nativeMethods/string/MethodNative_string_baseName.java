package org.bds.lang.nativeMethods.string;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_baseName extends MethodNativeString {

	private static final long serialVersionUID = 5638806991016067072L;


	public MethodNative_string_baseName() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "baseName";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String filePath = objThis.toString();
		if (filePath.isEmpty()) return "";
		Data data = bdsThread.data(filePath);
		return data.getName();
	}
}
