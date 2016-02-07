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
 * Return a list of values
 * 
 * @author pcingola
 */
public class MethodNativeMapValues extends MethodNativeMap {

	public MethodNativeMapValues(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "values";
		classType = TypeMap.get(baseType);
		returnType = TypeList.get(baseType);

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
		list.addAll(map.values());
		Collections.sort(list);
		return list;
	}
}
