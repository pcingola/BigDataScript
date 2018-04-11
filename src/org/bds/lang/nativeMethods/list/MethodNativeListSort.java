package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Sort: Create a new list and sort it
 *
 * @author pcingola
 */
public class MethodNativeListSort extends MethodNativeList {

	private static final long serialVersionUID = 165172407424014600L;

	public MethodNativeListSort(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "sort";
		returnType = classType;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		// Empty list? => Nothing to do
		ValueList sl = new ValueList(vthis.getType());
		if (vthis.isEmpty()) return sl;

		// Create new list and sort it
		sl.addAll(vthis);
		sl.sort();
		return sl;
	}
}
