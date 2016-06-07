package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.run.BdsThread;
import org.bds.task.Task;

public class MethodNative_string_isDone extends MethodNative {
	public MethodNative_string_isDone() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "isDone";
		classType = Type.STRING;
		returnType = Type.BOOL;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String taskId = objThis.toString();
		Task task = bdsThread.getTask(taskId);
		if (task == null) return false;
		return task.isDone();
	}
}
