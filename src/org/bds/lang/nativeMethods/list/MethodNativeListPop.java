package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.run.BdsThread;

/**
 * Head: return the first element of a list
 * 
 * @author pcingola
 */
public class MethodNativeListPop extends MethodNativeList {

	public MethodNativeListPop(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "pop";
		classType = TypeList.get(baseType);
		returnType = baseType;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		if (list.isEmpty()) throw new RuntimeException("Invoking 'pop' element on an empty list.");
		return list.remove(list.size() - 1);
	}
}
