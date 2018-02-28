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

public class MethodNative_string_length extends MethodNative {
	public MethodNative_string_length() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "length";
		classType = Types.STRING;
		returnType = Types.INT;

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		return (long) objThis.toString().length();
	}
}
