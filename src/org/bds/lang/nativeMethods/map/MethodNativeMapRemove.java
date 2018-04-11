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
public class MethodNativeMapRemove extends MethodNativeMap {

	private static final long serialVersionUID = 4507204640511249438L;

	public MethodNativeMapRemove(TypeMap mapType) {
		super(mapType);
	}

	@Override
	protected void initMethod() {
		TypeMap mapType = (TypeMap) classType;
		functionName = "remove";
		returnType = Types.BOOL;

		String argNames[] = { "this", "key" };
		Type argTypes[] = { mapType, mapType.getKeyType() };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	protected Value runMethodNative(BdsThread bdsThread, ValueMap vthis) {
		Value key = bdsThread.getValue("key");
		return new ValueBool(vthis.remove(key));
	}
}
