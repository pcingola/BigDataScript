package org.bds.lang.nativeMethods.list;

import java.util.List;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;

/**
 * Has: Check if an element exists in the list
 * 
 * @author karthik-rp
 */
public class MethodNativeListHas extends MethodNativeList {

	public MethodNativeListHas(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "has";
		returnType = Types.BOOL;

		String argNames[] = { "this", "toCheck" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		List list = (List) objThis;
		Value toCheck = bdsThread.getValue("toCheck");
		return list.contains(toCheck.get());
	}
}
