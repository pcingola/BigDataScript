package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.run.BdsThread;
import org.bds.run.BdsThreads;
import org.bds.task.Task;
import org.bds.util.Gpr;

public class MethodNative_string_stderr extends MethodNative {
	public MethodNative_string_stderr() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "stderr";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String taskId = objThis.toString();
		Task task = BdsThreads.getTask(taskId);
		if (task == null) return "";
		return Gpr.readFile(task.getStderrFile(), false);
	}
}
