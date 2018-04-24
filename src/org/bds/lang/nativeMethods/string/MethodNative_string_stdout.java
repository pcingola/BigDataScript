package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.task.Task;
import org.bds.util.Gpr;

public class MethodNative_string_stdout extends MethodNativeString {

	private static final long serialVersionUID = 6231366326569435136L;

	public MethodNative_string_stdout() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "stdout";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String taskId = objThis.toString();

		// Find task
		Task task = bdsThread.getTask(taskId);
		if (task == null) return "";

		// Find STDOUT file
		String stdoutFile = task.getStdoutFile();
		if (stdoutFile == null || !Gpr.exists(stdoutFile)) return "";

		// Read STDOUT file
		String stdout = Gpr.readFile(stdoutFile, false);
		return stdout == null ? "" : stdout;
	}
}
