package org.bds.lang.nativeMethods.map;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueMap;
import org.bds.run.BdsThread;

/**
 * Return a value, or a default value if key is not found
 *
 * @author pcingola
 */
public class MethodNativeMapGet extends MethodNativeMap {

	private static final long serialVersionUID = 6994418128706504344L;

	public MethodNativeMapGet(TypeMap mapType) {
		super(mapType);
	}

	@Override
	protected void initMethod() {
		TypeMap mapType = (TypeMap) classType;
		functionName = "get";
		returnType = mapType.getValueType();

		String argNames[] = { "this", "key", "defaultValue" };
		Type argTypes[] = { mapType, mapType.getKeyType(), returnType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	protected Value runMethodNative(BdsThread bdsThread, ValueMap vthis) {
		Value key = bdsThread.getValue("key");
		Value v = vthis.getValue(key);
		if (v == null) v = bdsThread.getValue("defaultValue");
		return v;
	}

}
