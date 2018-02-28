package org.bds.lang.nativeMethods.string;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.bds.lang.Parameters;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.run.BdsThread;
import org.bds.task.Task;
import org.bds.util.Gpr;

public class MethodNative_string_endsWith_str extends MethodNative {
	public MethodNative_string_endsWith_str() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "endsWith";
		classType = Type.STRING;
		returnType = Type.BOOL;

		String argNames[] = { "this", "str" };
		Type argTypes[] = { Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return objThis.toString().endsWith( csThread.getString("str") );
	}
}
