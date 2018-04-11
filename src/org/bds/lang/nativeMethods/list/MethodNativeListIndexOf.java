package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Add: Return the index of an element in the list (-1 if not found)
 *
 * @author pcingola
 */
public class MethodNativeListIndexOf extends MethodNativeList {

	private static final long serialVersionUID = -3244209394215672718L;

	public MethodNativeListIndexOf(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "indexOf";
		returnType = Types.INT;

		String argNames[] = { "this", "toFind" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		Value toFind = bdsThread.getValue("toFind");
		long idx = vthis.indexOf(toFind);
		return new ValueInt(idx);
	}
}
