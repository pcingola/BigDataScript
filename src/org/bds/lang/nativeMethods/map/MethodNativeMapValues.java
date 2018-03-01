package org.bds.lang.nativeMethods.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.TypeMap;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

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
		classType = TypeMap.get(Types.ANY, baseType);
		returnType = TypeList.get(baseType);

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		Map map = (Map) objThis;
		List list = new ArrayList();
		list.addAll(map.values());
		Collections.sort(list);
		return list;
	}
}
