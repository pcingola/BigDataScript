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
 * Add: Remove an element from the list
 *
 * @author pcingola
 */
public class MethodNativeListCount extends MethodNativeList {

	private static final long serialVersionUID = 3750856247316038370L;

	public MethodNativeListCount(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "count";
		returnType = Types.INT;

		String argNames[] = { "this", "toCount" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		Value toCount = bdsThread.getValue("toCount");
		long count = 0;
		for (Value v : vthis)
			if (toCount.equals(v)) count++;

		return new ValueInt(count);
	}
}
