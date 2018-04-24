package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.task.Task;

public class MethodNative_string_isDoneOk extends MethodNativeString {

	private static final long serialVersionUID = 6853549814647390208L;

	public MethodNative_string_isDoneOk() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "isDoneOk";
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
		return task.isDoneOk();
	}
}
