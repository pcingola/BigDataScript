package org.bigDataScript.lang.nativeMethods.list;

import java.util.List;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;
import org.bigDataScript.run.BdsThread;

/**
 * RmOnExit: Mark all files to be deleted on exit
 * 
 * @author pcingola
 */
public class MethodNativeListRmOnExit extends MethodNativeList {

	public MethodNativeListRmOnExit(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "rmOnExit";
		classType = TypeList.get(baseType);
		returnType = Type.VOID;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		List list = (List) objThis;
		bdsThread.rmOnExit(list);
		return objThis;
	}
}
