package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Has: Check if an element exists in the list
 *
 * @author karthik-rp
 */
public class MethodNativeListHas extends MethodNativeList {

	private static final long serialVersionUID = 989799140756758369L;

	public MethodNativeListHas(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "has";
		returnType = Types.BOOL;

		String argNames[] = { "this", "toCheck" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		Value toCheck = bdsThread.getValue("toCheck");
		return new ValueBool(vthis.contains(toCheck));
	}
}
