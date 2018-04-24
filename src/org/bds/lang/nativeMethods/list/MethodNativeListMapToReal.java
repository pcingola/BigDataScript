package org.bds.lang.nativeMethods.list;

import org.bds.lang.type.TypeList;

/**
 * Map: Apply a function to all elements in the list
 *
 * @author pcingola
 */
public class MethodNativeListMapToReal extends MethodNativeListMap {

	private static final long serialVersionUID = 1262602106599145472L;


	public MethodNativeListMapToReal(TypeList listType) {
		super(listType);
	}

	//	@Override
	//	protected void initMethod(Type baseType) {
	//		super.initMethod(baseType);
	//		functionName = "mapToReal";
	//		returnBaseType = Types.REAL;
	//		returnType = TypeList.get(returnBaseType);
	//	}

}
