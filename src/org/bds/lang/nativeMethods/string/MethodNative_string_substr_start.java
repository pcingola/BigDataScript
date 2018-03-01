package org.bds.lang.nativeMethods.string;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.bds.lang.Parameters;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.task.Task;
import org.bds.util.Gpr;

public class MethodNative_string_substr_start extends MethodNativeString {
	public MethodNative_string_substr_start() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "substr";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this", "start" };
		Type argTypes[] = { Types.STRING, Types.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		String s = objThis.toString(); int start = (int) csThread.getInt("start") ; start=Math.max(0,start); return s.substring(start);
	}
}
