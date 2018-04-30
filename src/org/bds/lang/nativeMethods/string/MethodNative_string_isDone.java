package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.task.Task;

public class MethodNative_string_isDone extends MethodNativeString {

	private static final long serialVersionUID = -798989191605034201L;

	public MethodNative_string_isDone() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "isDone";
		classType = Types.STRING;
		returnType = Types.BOOL;

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
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
