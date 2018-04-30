package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_dirName extends MethodNativeString {

	private static final long serialVersionUID = -7157118072886138111L;

	public MethodNative_string_dirName() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "dirName";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		try {
			String d = (bdsThread.data(objThis.toString())).getParent();
			return d != null ? d : ".";
		} catch (Exception e) {
			return "";
		}
	}
}
