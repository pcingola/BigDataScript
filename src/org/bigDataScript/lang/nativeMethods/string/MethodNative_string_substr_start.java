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

public class MethodNative_string_substr_start extends MethodNative {
	public MethodNative_string_substr_start() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "substr";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this", "start" };
		Type argTypes[] = { Type.STRING, Type.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		String s = objThis.toString(); int start = (int) csThread.getInt("start") ; start=Math.max(0,start); return s.substring(start);
	}
}
