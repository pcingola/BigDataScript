package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.List;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

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
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		List list = (List) objThis;
		csThread.rmOnExit(list);
		return objThis;
	}
}
