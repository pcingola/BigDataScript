package org.bds.lang.nativeMethods.map;

import java.util.HashMap;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

/**
 * Return a list of keys
 * 
 * @author pcingola
 */
public class MethodNativeMapHasKey extends MethodNativeMap {

	public MethodNativeMapHasKey(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "hasKey";
		classType = TypeMap.get(baseType);
		returnType = Types.BOOL;

		String argNames[] = { "this", "key" };
		Type argTypes[] = { classType, Types.STRING }; // null: don't check argument (anything can be converted to 'string')
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		HashMap map = (HashMap) objThis;
		String key = csThread.getObject("key").toString();
		return map.containsKey(key);
	}
}
