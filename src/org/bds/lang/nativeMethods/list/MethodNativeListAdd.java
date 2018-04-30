package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Add: Add an element to the end of the list
 *
 * Note: Exactly the same as push
 *
 * @author pcingola
 */
public class MethodNativeListAdd extends MethodNativeList {

	private static final long serialVersionUID = -8317711972253566041L;

	public MethodNativeListAdd(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "add";
		returnType = classType;

		String argNames[] = { "this", "toPush" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		Value toPush = bdsThread.getValue("toPush");
		vthis.add(toPush);
		return vthis;
	}
}
