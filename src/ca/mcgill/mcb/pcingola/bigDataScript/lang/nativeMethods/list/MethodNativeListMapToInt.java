package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;

/**
 * Map: Apply a function to all elements in the list
 *
 * @author pcingola
 */
public class MethodNativeListMapToInt extends MethodNativeListMap {

	public MethodNativeListMapToInt(Type baseType) {
		super(baseType, Type.INT, "mapToInt");
	}

}
