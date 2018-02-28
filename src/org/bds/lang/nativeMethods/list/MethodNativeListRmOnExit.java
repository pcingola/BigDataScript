package org.bds.lang.nativeMethods.list;

import java.util.List;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.run.BdsThread;

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
