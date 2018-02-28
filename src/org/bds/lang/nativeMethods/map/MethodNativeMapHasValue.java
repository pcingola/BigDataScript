package org.bds.lang.nativeMethods.map;

import java.util.HashMap;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.run.BdsThread;

/**
 * Return a list of keys
 * 
 * @author pcingola
 */
public class MethodNativeMapHasValue extends MethodNativeMap {

	public MethodNativeMapHasValue(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "hasValue";
		classType = TypeMap.get(baseType);
		returnType = Type.BOOL;

		String argNames[] = { "this", "val" };
		Type argTypes[] = { classType, baseType }; // null: don't check argument (anything can be converted to 'string')
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		HashMap map = (HashMap) objThis;
		Object val = csThread.getObject("val");
		return map.containsValue(val);
	}
}
