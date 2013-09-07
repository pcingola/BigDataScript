package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.map;

import java.util.HashMap;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeMap;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Return a list of keys
 * 
 * @author pcingola
 */
public class MethodNativeMapHasValue extends MethodNativeMap {

	public MethodNativeMapHasValue(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "hasValue";
		classType = TypeMap.get(baseType);
		returnType = Type.BOOL;

		String argNames[] = { "this", "val" };
		Type argTypes[] = { classType, baseType }; // null: don't check argument (anything can be converted to 'string')
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		HashMap map = (HashMap) objThis;
		Object val = csThread.getObject("val");
		return map.containsValue(val);
	}
}
