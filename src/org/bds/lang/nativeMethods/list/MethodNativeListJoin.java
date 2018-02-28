package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.run.BdsThread;

/**
 * Join all elements of a ins into a string
 * 
 * @author pcingola
 */
public class MethodNativeListJoin extends MethodNativeList {

	public MethodNativeListJoin(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "join";
		classType = TypeList.get(baseType);
		returnType = Type.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings("rawtypes")
	String join(ArrayList list, String str) {
		StringBuilder sb = new StringBuilder();

		if (list.isEmpty()) return "";

		int max = list.size() - 1;
		for (int i = 0; i < max; i++)
			sb.append(list.get(i) + str);
		sb.append(list.get(max));

		return sb.toString();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		return join(list, " ");
	}
}
