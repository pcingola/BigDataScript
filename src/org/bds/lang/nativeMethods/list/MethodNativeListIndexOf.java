package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.run.BdsThread;

/**
 * Add: Return the index of an element in the list (-1 if not found)
 *
 * @author pcingola
 */
public class MethodNativeListIndexOf extends MethodNativeList {

	public MethodNativeListIndexOf(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "indexOf";
		classType = TypeList.get(baseType);
		returnType = Type.INT;

		String argNames[] = { "this", "toFind" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		Object toFind = csThread.getObject("toFind");

		long idx = list.indexOf(toFind);
		return idx;
	}
}
