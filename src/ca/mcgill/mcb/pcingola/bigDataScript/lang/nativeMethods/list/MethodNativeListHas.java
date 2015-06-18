package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Has: Check if an element exists in the list
 * 
 * 
 * @author karthik-rp
 */
public class MethodNativeListHas extends MethodNativeList {

	public MethodNativeListHas(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "has";
		classType = TypeList.get(baseType);
		returnType = Type.INT;

		String argNames[] = { "this", "toCheck" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		Object toCheck = csThread.getObject("toCheck");
		if (toCheck == null) {
			for (Object val : list) {
				if (val == null) return true;
			}
		} else {
			for (Object val : list) {
				if (toCheck.equals(val)) return true;
			}
		}
		return false;
	}
}
