package org.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;
import org.bigDataScript.run.BdsThread;

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
		returnType = Type.BOOL;

		String argNames[] = { "this", "toCheck" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		Object toCheck = csThread.getObject("toCheck");
		return list.contains(toCheck);
	}
}
