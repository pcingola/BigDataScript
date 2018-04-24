package org.bds.lang.nativeMethods.list;

import org.bds.lang.type.TypeList;

/**
 * Map: Apply a function to all elements in the list
 *
 * @author pcingola
 */
public class MethodNativeListMapToInt extends MethodNativeListMap {

	private static final long serialVersionUID = 1256239877054300160L;


	public MethodNativeListMapToInt(TypeList listType) {
		super(listType);
		//		super(listType, Types.INT, "mapToInt");
	}

}
