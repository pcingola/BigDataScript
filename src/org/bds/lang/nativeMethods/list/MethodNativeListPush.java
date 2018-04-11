package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Push: Add an element to the end of the list
 *
 * @author pcingola
 */
public class MethodNativeListPush extends MethodNativeList {

	private static final long serialVersionUID = 4083084613330119066L;

	public MethodNativeListPush(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "push";
		returnType = baseType;

		String argNames[] = { "this", "toPush" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		Value toPush = bdsThread.getValue("toPush");
		vthis.add(toPush);
		return toPush;
	}
}
