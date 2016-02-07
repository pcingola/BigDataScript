package org.bigDataScript.lang.nativeMethods.list;

import java.util.List;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;
import org.bigDataScript.run.BdsThread;

/**
 * Rm: Delete all files in list: same as string[].rm()
 * 
 * @author pcingola
 */
public class MethodNativeListDelete extends MethodNativeList {

	public MethodNativeListDelete(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "delete";
		classType = TypeList.get(baseType);
		returnType = Type.VOID;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		List list = (List) objThis;

		for (Object o : list)
			bdsThread.data(o.toString()).delete();

		return objThis;
	}
}
