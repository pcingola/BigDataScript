package org.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;
import org.bigDataScript.run.BdsThread;

/**
 * List's length (number of elements)
 * 
 * @author pcingola
 */
public class MethodNativeListSize extends MethodNativeList {

	public MethodNativeListSize(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "size";
		classType = TypeList.get(baseType);
		returnType = Type.INT;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		return (long) list.size();
	}
}
