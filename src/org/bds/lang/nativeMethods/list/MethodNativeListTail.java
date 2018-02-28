package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;

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

	public MethodNativeListTail(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "tail";
		classType = TypeList.get(baseType);
		returnType = TypeList.get(baseType);

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;

		// Empty list or only one element? => Nothing to do
		if (list.size() <= 1) return new ArrayList();

		// Create new list
		ArrayList newList = new ArrayList(list.size() - 1);

		// Add all but first elements from list
		int idx = 0;
		for (Object o : list)
			if ((idx++) > 0) newList.add(o);

		return newList;
	}
}
