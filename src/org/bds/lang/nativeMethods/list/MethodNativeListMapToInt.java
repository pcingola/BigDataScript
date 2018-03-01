package org.bds.lang.nativeMethods.list;

import org.bds.lang.type.TypeList;

/**
 * Map: Apply a function to all elements in the list
 *
 * @author pcingola
 */
public class MethodNativeListMapToInt extends MethodNativeListMap {

	public MethodNativeListMapToInt(TypeList listType) {
		super(listType);
		//		super(listType, Types.INT, "mapToInt");
	}

}
