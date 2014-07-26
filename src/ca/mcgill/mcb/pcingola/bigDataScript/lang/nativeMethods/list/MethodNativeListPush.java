package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Push: Add an element to the end of the list
 * 
 * @author pcingola
 */
public class MethodNativeListPush extends MethodNativeList {

	public MethodNativeListPush(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {

		functionName = "push";
		classType = TypeList.get(baseType);
		returnType = baseType;

		String argNames[] = { "this", "toPush" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		Object toPush = csThread.getObject("toPush");
		list.add(toPush);
		return toPush;
	}
}
