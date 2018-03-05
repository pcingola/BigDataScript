package org.bds.lang.nativeMethods.list;

import java.util.List;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;

/**
 * Push: Add an element to the end of the list
 * 
 * @author pcingola
 */
public class MethodNativeListPush extends MethodNativeList {

	public MethodNativeListPush(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "push";
		returnType = baseType;

		String argNames[] = { "this", "toPush" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		List list = (List) objThis;
		Value toPush = bdsThread.getValue("toPush");
		list.add(toPush.get());
		return toPush;
	}
}
