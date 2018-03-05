package org.bds.lang.nativeMethods.map;

import java.util.Map;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;

/**
 * Return a list of keys
 * 
 * @author pcingola
 */
public class MethodNativeMapHasKey extends MethodNativeMap {

	public MethodNativeMapHasKey(TypeMap mapType) {
		super(mapType);
	}

	@Override
	protected void initMethod() {
		TypeMap mapType = (TypeMap) classType;
		functionName = "hasKey";
		returnType = Types.BOOL;

		String argNames[] = { "this", "key" };
		Type argTypes[] = { mapType, mapType.getKeyType() };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		Map map = (Map) objThis;
		Value key = bdsThread.getValue("key");
		return map.containsKey(key.get());
	}
}
