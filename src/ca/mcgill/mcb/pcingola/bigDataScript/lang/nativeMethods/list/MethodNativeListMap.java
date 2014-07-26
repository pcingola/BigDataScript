package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Map: Apply a function to all elements in the list
 * 
 * @author pcingola
 */
public class MethodNativeListMap extends MethodNativeList {

	public MethodNativeListMap(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "map";
		classType = TypeList.get(baseType);
		returnType = baseType;

		String argNames[] = { "this", "f" };
		Type argTypes[] = { classType, Type.FUNC };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		// Object function = csThread.getObject("f");
		return list;
	}
}
