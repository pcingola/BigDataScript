package org.bds.lang.nativeMethods.list;

import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;

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
		returnBaseType = Types.STRING;
		returnType = TypeList.get(returnBaseType);
	}

}
