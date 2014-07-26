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
public class MethodNativeMapSize extends MethodNativeMap {

	public MethodNativeMapSize(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "size";
		classType = TypeMap.get(baseType);
		returnType = Type.INT;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		HashMap map = (HashMap) objThis;
		return (long) map.size();
	}
}
