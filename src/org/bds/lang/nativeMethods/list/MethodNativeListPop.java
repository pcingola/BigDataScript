package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Head: return the first element of a list
 *
 * @author pcingola
 */
public class MethodNativeListPop extends MethodNativeList {

	private static final long serialVersionUID = 2069592663054842713L;

	public MethodNativeListPop(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "pop";
		returnType = baseType;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		if (vthis.isEmpty()) throw new RuntimeException("Invoking 'pop' on an empty list.");
		return vthis.remove(vthis.size() - 1);
	}
}
