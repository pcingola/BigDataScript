package org.bds.lang.nativeFunctions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bds.executioner.Executioner;
import org.bds.executioner.Executioners;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

/**
 * Native function "countTasks": Count number of tasks (running or pending)
 *
 * @author pcingola
 */
public class FunctionNativeGetTasksRunning extends FunctionNative {

	private static final long serialVersionUID = 1005687259103280710L;

	public FunctionNativeGetTasksRunning() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "countTasks";
		returnType = TypeList.get(Types.STRING);

		String argNames[] = {};
		Type argTypes[] = {};
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		List<String> taskIds = new ArrayList<>();
		for (Executioner ex : Executioners.getInstance().getAll()) {
			taskIds.addAll(ex.getTasksRunning().keySet());
		}
		Collections.sort(taskIds);
		return taskIds;
	}
}
