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

public class MethodNative_string_parseInt extends MethodNative {
	public MethodNative_string_parseInt() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "parseInt";
		classType = Type.STRING;
		returnType = Type.INT;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return Gpr.parseLongSafe( objThis.toString().trim() );
	}
}
