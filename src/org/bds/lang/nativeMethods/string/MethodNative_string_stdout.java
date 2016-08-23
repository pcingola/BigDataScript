package org.bds.lang.nativeMethods.string;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.TypeList;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.run.BdsThread;
import org.bds.run.BdsThreads;
import org.bds.task.Task;
import org.bds.util.Gpr;

public class MethodNative_string_stdout extends MethodNative {
	public MethodNative_string_stdout() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "stdout";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		String taskId = objThis.toString();
		Task task = BdsThreads.getTask(taskId);
		if (task == null) return "";
		return Gpr.readFile(task.getStdoutFile(), false);
	}
}
