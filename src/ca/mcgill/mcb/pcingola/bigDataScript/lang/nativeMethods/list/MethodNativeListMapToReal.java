package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;

/**
 * Map: Apply a function to all elements in the list
 *
 * @author pcingola
 */
public class MethodNativeListMapToReal extends MethodNativeListMap {

	public MethodNativeListMapToReal(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		super.initMethod(baseType);
		functionName = "mapToReal";
		returnBaseType = Type.REAL;
		returnType = TypeList.get(returnBaseType);
	}

}
