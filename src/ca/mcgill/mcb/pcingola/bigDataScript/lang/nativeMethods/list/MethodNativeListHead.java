package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

/**
 * Head: return the first element of a list
 * 
 * @author pcingola
 */
public class MethodNativeListHead extends MethodNativeList {

	public MethodNativeListHead(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "head";
		classType = TypeList.get(baseType);
		returnType = baseType;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = ((ArrayList) objThis);
		if (list.isEmpty()) throw new RuntimeException("Invoking 'head' on an empty list.");
		return ((ArrayList) objThis).get(0);
	}
}
