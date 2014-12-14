package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;

public class MethodNative_string_isDoneOk extends MethodNative {
	public MethodNative_string_isDoneOk() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "isDoneOk";
		classType = Type.STRING;
		returnType = Type.BOOL;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		String taskId = objThis.toString(); Task task = csThread.getTask(taskId); if (task == null) return false; return task.isDoneOk();
	}
}