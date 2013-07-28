package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * List's length (number of elements)
 * 
 * @author pcingola
 */
public class MethodNativeListIsEmpty extends MethodNativeList {

	public MethodNativeListIsEmpty(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "isEmpty";
		classType = TypeList.get(baseType);
		returnType = Type.BOOL;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		return list.isEmpty();
	}
}
