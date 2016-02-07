package org.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;
import org.bigDataScript.run.BdsThread;

/**
 * Add: Remove an element from the list
 *
 * @author pcingola
 */
public class MethodNativeListRemove extends MethodNativeList {

	public MethodNativeListRemove(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "remove";
		classType = TypeList.get(baseType);
		returnType = baseType;

		String argNames[] = { "this", "toRemove" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		Object toRemove = csThread.getObject("toRemove");
		list.remove(toRemove);
		return toRemove;
	}
}
