package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;
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
public class MethodNativeListClone extends MethodNativeList {

	public MethodNativeListClone(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "clone";
		returnType = classType;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		List list = (List) objThis;
		List newList = new ArrayList(list.size());
		newList.addAll(list);
		return newList;
	}
}
