package org.bds.lang.nativeMethods.list;

import java.util.List;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

/**
 * Add: Return the index of an element in the list (-1 if not found)
 *
 * @author pcingola
 */
public class MethodNativeListIndexOf extends MethodNativeList {

	public MethodNativeListIndexOf(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "indexOf";
		returnType = Types.INT;

		String argNames[] = { "this", "toFind" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		List list = (List) objThis;
		Object toFind = bdsThread.getObject("toFind");

		long idx = list.indexOf(toFind);
		return idx;
	}
}
