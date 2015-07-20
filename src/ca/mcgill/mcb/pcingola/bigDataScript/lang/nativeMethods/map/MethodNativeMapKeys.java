package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeMap;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

/**
 * Return a list of keys
 * 
 * @author pcingola
 */
public class MethodNativeMapKeys extends MethodNativeMap {

	public MethodNativeMapKeys(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "keys";
		classType = TypeMap.get(baseType);
		returnType = TypeList.get(Type.STRING);

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		HashMap map = (HashMap) objThis;
		ArrayList list = new ArrayList();
		list.addAll(map.keySet());
		Collections.sort(list);
		return list;
	}
}
