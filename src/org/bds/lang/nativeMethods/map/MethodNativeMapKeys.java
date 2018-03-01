package org.bds.lang.nativeMethods.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.TypeMap;
import org.bds.run.BdsThread;

/**
 * Return a list of keys
 *
 * @author pcingola
 */
public class MethodNativeMapKeys extends MethodNativeMap {

	public MethodNativeMapKeys(TypeMap mapType) {
		super(mapType);
	}

	@Override
	protected void initMethod() {
		functionName = "keys";
		classType = mapType;
		returnType = TypeList.get(mapType.getKeyType());

		String argNames[] = { "this" };
		Type argTypes[] = { mapType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		Map map = (Map) objThis;
		List list = new ArrayList();
		list.addAll(map.keySet());
		Collections.sort(list);
		return list;
	}
}
