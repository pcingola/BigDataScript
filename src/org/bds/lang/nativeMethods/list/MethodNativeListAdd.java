package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.run.BdsThread;

/**
 * Add: Add an element to the end of the list
 * 
 * Note: Exactly the same as push
 * 
 * @author pcingola
 */
public class MethodNativeListAdd extends MethodNativeList {

	public MethodNativeListAdd(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "add";
		classType = TypeList.get(baseType);
		returnType = baseType;

		String argNames[] = { "this", "toPush" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		Object toPush = csThread.getObject("toPush");
		list.add(toPush);
		return toPush;
	}
}
