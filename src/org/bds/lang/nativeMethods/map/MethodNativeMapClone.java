package org.bds.lang.nativeMethods.map;

import java.util.HashMap;
import java.util.Map;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.run.BdsThread;

/**
 * Return a list of keys
 *
 * @author pcingola
 */
public class MethodNativeMapClone extends MethodNativeMap {

	public MethodNativeMapClone(TypeMap mapType) {
		super(mapType);
	}

	@Override
	protected void initMethod() {
		TypeMap mapType = (TypeMap) classType;
		functionName = "keys";
		returnType = mapType;

		String argNames[] = { "this" };
		Type argTypes[] = { mapType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		Map map = (Map) objThis;
		Map newMap = new HashMap(map.size());
		newMap.putAll(map);
		return newMap;
	}
}
