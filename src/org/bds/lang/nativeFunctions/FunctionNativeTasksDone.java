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
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Native function "tasksDone": Return a list of all taskIds that have finished
 *
 * @author pcingola
 */
public class FunctionNativeTasksDone extends FunctionNative {

	private static final long serialVersionUID = 1005687259103280710L;

	public FunctionNativeTasksDone() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "tasksDone";
		returnType = TypeList.get(Types.STRING);

		String argNames[] = {};
		Type argTypes[] = {};
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	public Value runFunction(BdsThread bdsThread) {
		// Get all taskIds sorted
		List<String> taskIds = new ArrayList<>();
		for (Executioner ex : Executioners.getInstance().getAll())
			taskIds.addAll(ex.getTaskIdsDone());
		Collections.sort(taskIds);

		// Convert into a list of strings
		ValueList valueTaskIds = (ValueList) returnType.newValue();
		for (String tid : taskIds)
			valueTaskIds.add(Value.factory(tid));

		return valueTaskIds;
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		throw new RuntimeException("Unimplemented. This method should never be invoked!");
	}
}
