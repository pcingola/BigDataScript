package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;

/**
 * Add: Add an element to the list (position 'idx')
 * 
 * @author pcingola
 */
public class MethodNativeListAddIndex extends MethodNativeList {

	public MethodNativeListAddIndex(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "add";
		returnType = baseType;

		String argNames[] = { "this", "idx", "toPush" };
		Type argTypes[] = { classType, Types.INT, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		long idx = bdsThread.getInt("idx");
		Value toPush = bdsThread.getValue("toPush");
		Object v = toPush.get();
		list.add((int) idx, v);
		return v;
	}
}
