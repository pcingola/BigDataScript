package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;
import java.util.List;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.run.BdsThread;

/**
 * Tail: Create a new list with all the elements but the first
 * 
 * @author pcingola
 */
public class MethodNativeListTail extends MethodNativeList {

	public MethodNativeListTail(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "tail";
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

		// Empty list or only one element? => Nothing to do
		if (list.size() <= 1) return new ArrayList();

		// Create new list
		List newList = new ArrayList(list.size() - 1);

		// Add all but first elements from list
		int idx = 0;
		for (Object o : list)
			if ((idx++) > 0) newList.add(o);

		return newList;
	}
}
