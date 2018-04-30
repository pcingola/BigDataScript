package org.bds.lang.nativeMethods.map;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueMap;
import org.bds.run.BdsThread;

/**
 * Return a list of keys
 *
 * @author pcingola
 */
public class MethodNativeMapClone extends MethodNativeMap {

	private static final long serialVersionUID = -6953764021982952187L;

	public MethodNativeMapClone(TypeMap mapType) {
		super(mapType);
	}

	@Override
	protected void initMethod() {
		TypeMap mapType = (TypeMap) classType;
		functionName = "clone";
		returnType = mapType;

		String argNames[] = { "this" };
		Type argTypes[] = { mapType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	protected Value runMethodNative(BdsThread bdsThread, ValueMap vthis) {
		return vthis.clone();
	}
}
