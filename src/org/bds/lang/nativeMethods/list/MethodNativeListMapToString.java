package org.bds.lang.nativeMethods.list;

import org.bds.lang.type.TypeList;

/**
 * Map: Apply a function to all elements in the list
 *
 * @author pcingola
 */
public class MethodNativeListMapToString extends MethodNativeListMap {

	private static final long serialVersionUID = 5218774077971398656L;


	public MethodNativeListMapToString(TypeList listType) {
		super(listType);
	}

	//	@Override
	//	protected void initMethod(Type baseType) {
	//		super.initMethod(baseType);
	//		functionName = "mapToString";
	//		returnBaseType = Types.STRING;
	//		returnType = TypeList.get(returnBaseType);
	//	}

}
