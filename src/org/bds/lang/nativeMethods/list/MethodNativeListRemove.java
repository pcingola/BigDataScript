package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.run.BdsThread;

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
