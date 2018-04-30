package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Add: Add an element to the list (position 'idx')
 *
 * @author pcingola
 */
public class MethodNativeListAddIndex extends MethodNativeList {

	private static final long serialVersionUID = 6579215395739141983L;

	public MethodNativeListAddIndex(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "add";
		returnType = baseType;

		String argNames[] = { "this", "idx", "toPush" };
		Type argTypes[] = { classType, Types.INT, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		long idx = bdsThread.getInt("idx");
		Value toPush = bdsThread.getValue("toPush");
		vthis.add((int) idx, toPush);
		return toPush;
	}

}
