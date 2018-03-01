package org.bds.lang.nativeMethods.list;

import java.util.List;

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
public class MethodNativeListAddList extends MethodNativeList {

	public MethodNativeListAddList(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "add";
		returnType = baseType;

		String argNames[] = { "this", "toPush" };
		Type argTypes[] = { classType, TypeList.get(baseType) };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		List list = (List) objThis;
		List toPush = (List) bdsThread.getObject("toPush");
		list.addAll(toPush);
		return toPush;
	}
}
