package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.run.BdsThread;
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

		// Find task
		Task task = bdsThread.getTask(taskId);
		if (task == null) return "";

		// Find STDERR file
		String stderrFile = task.getStderrFile();
		if (stderrFile == null || !Gpr.exists(stderrFile)) return "";

		// Read STDERR file
		String stderr = Gpr.readFile(stderrFile, false);
		return stderr == null ? "" : stderr;
	}
}
