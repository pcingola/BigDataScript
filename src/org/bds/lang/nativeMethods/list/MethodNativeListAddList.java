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
public class MethodNativeListAddList extends MethodNativeList {

	private static final long serialVersionUID = 1709723665361397991L;

	public MethodNativeListAddList(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "add";
		returnType = baseType;

		String argNames[] = { "this", "toPush" };
		Type argTypes[] = { classType, TypeList.get(baseType) };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		ValueList toPush = (ValueList) bdsThread.getValue("toPush");
		vthis.addAll(toPush);
		return vthis;
	}
}
