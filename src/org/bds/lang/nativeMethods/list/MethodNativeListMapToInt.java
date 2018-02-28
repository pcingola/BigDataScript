package org.bds.lang.nativeMethods.list;

import org.bds.lang.type.Type;
import org.bds.lang.type.Types;

/**
 * Map: Apply a function to all elements in the list
 *
 * @author pcingola
 */
public class MethodNativeListMapToInt extends MethodNativeListMap {

	public MethodNativeListMapToInt(Type baseType) {
		super(baseType, Types.INT, "mapToInt");
	}

}
