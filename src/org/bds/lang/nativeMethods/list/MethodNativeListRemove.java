package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Add: Remove an element from the list
 *
 * @author pcingola
 */
public class MethodNativeListRemove extends MethodNativeList {

	private static final long serialVersionUID = -3111970920223868244L;

	public MethodNativeListRemove(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "remove";
		returnType = baseType;

		String argNames[] = { "this", "toRemove" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		Value toRemove = bdsThread.getValue("toRemove");
		vthis.remove(toRemove);
		return toRemove;
	}
}
