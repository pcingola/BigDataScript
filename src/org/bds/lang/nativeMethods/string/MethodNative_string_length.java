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

public class MethodNative_string_length extends MethodNative {
	public MethodNative_string_length() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "length";
		classType = Type.STRING;
		returnType = Type.INT;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return (long) objThis.toString().length();
	}
}
