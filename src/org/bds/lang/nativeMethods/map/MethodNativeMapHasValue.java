package org.bds.lang.nativeMethods.map;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueMap;
import org.bds.run.BdsThread;

/**
 * Return a list of keys
 *
 * @author pcingola
 */
public class MethodNativeMapHasValue extends MethodNativeMap {

	private static final long serialVersionUID = -4582217604586185197L;

	public MethodNativeMapHasValue(TypeMap mapType) {
		super(mapType);
	}

	@Override
	protected void initMethod() {
		TypeMap mapType = (TypeMap) classType;
		functionName = "hasValue";
		returnType = Types.BOOL;

		String argNames[] = { "this", "val" };
		Type argTypes[] = { mapType, mapType.getKeyType() };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	protected Value runMethodNative(BdsThread bdsThread, ValueMap vthis) {
		Value val = bdsThread.getValue("val");
		return new ValueBool(vthis.containsValue(val));
	}
}
