package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

/**
 * Add: Add an element to the list (position 'idx')
 * 
 * @author pcingola
 */
public class MethodNativeListAddIndex extends MethodNativeList {

	public MethodNativeListAddIndex(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "add";
		classType = TypeList.get(baseType);
		returnType = baseType;

		String argNames[] = { "this", "idx", "toPush" };
		Type argTypes[] = { classType, Types.INT, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		long idx = csThread.getInt("idx");
		Object toPush = csThread.getObject("toPush");
		list.add((int) idx, toPush);
		return toPush;
	}
}
