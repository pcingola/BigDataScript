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
public class MethodNativeListHead extends MethodNativeList {

	private static final long serialVersionUID = -9109867503215538994L;

	public MethodNativeListHead(TypeList baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "head";
		returnType = baseType;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		if (vthis.isEmpty()) throw new RuntimeException("Invoking 'head' on an empty list.");
		return vthis.getValue(0);
	}
}
