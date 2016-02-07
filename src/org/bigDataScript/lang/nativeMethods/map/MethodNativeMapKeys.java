package org.bigDataScript.lang.nativeMethods.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;
import org.bigDataScript.lang.TypeMap;
import org.bigDataScript.run.BdsThread;

/**
 * Return a list of keys
 * 
 * @author pcingola
 */
public class MethodNativeMapKeys extends MethodNativeMap {

	public MethodNativeMapKeys(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "keys";
		classType = TypeMap.get(baseType);
		returnType = TypeList.get(Type.STRING);

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		HashMap map = (HashMap) objThis;
		ArrayList list = new ArrayList();
		list.addAll(map.keySet());
		Collections.sort(list);
		return list;
	}
}
