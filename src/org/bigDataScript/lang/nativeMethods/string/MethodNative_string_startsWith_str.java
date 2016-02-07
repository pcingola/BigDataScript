package org.bigDataScript.lang.nativeMethods.string;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;
import org.bigDataScript.lang.nativeMethods.MethodNative;
import org.bigDataScript.run.BdsThread;
import org.bigDataScript.task.Task;
import org.bigDataScript.util.Gpr;

public class MethodNative_string_startsWith_str extends MethodNative {
	public MethodNative_string_startsWith_str() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "startsWith";
		classType = Type.STRING;
		returnType = Type.BOOL;

		String argNames[] = { "this", "str" };
		Type argTypes[] = { Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return objThis.toString().startsWith( csThread.getString("str") );
	}
}
