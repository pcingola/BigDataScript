package org.bigDataScript.lang.nativeMethods.list;

import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;

/**
 * Map: Apply a function to all elements in the list
 *
 * @author pcingola
 */
public class MethodNativeListMapToString extends MethodNativeListMap {

	public MethodNativeListMapToString(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		super.initMethod(baseType);
		functionName = "mapToString";
		returnBaseType = Type.STRING;
		returnType = TypeList.get(returnBaseType);
	}

}
