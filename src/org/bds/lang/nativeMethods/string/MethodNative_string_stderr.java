package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.task.Task;
import org.bds.util.Gpr;

public class MethodNative_string_stderr extends MethodNativeString {

	private static final long serialVersionUID = 8164410211759587328L;

	public MethodNative_string_stderr() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "stderr";
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

		// Find STDERR file
		String stderrFile = task.getStderrFile();
		if (stderrFile == null || !Gpr.exists(stderrFile)) return "";

		// Read STDERR file
		String stderr = Gpr.readFile(stderrFile, false);
		return stderr == null ? "" : stderr;
	}
}
