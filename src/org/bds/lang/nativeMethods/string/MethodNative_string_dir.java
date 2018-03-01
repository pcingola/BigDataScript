package org.bds.lang.nativeMethods.string;

import java.util.ArrayList;
import java.util.Collections;

import org.bds.lang.Parameters;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_dir extends MethodNativeString {
	public MethodNative_string_dir() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "dir";
		classType = Types.STRING;
		returnType = TypeList.get(Types.STRING);

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		ArrayList<String> list = bdsThread.data(objThis.toString()).list();
		Collections.sort(list);
		return list;
	}
}
