package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.run.BdsThread;
import org.bds.run.BdsThreads;
import org.bds.task.Task;

public class MethodNative_string_exitCode extends MethodNative {
	public MethodNative_string_exitCode() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "exitCode";
		classType = Type.STRING;
		returnType = Type.INT;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String taskId = objThis.toString();
		Task task = BdsThreads.getTask(taskId);
		if (task == null) return 0L;
		return (long) task.getExitValue();
	}
}
