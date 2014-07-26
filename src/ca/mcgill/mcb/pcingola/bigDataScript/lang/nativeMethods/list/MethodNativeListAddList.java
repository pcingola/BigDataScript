package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Add: Add an element to the end of the list
 * 
 * Note: Exactly the same as push
 * 
 * @author pcingola
 */
public class MethodNativeListAddList extends MethodNativeList {

	public MethodNativeListAddList(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "add";
		classType = TypeList.get(baseType);
		returnType = baseType;

		String argNames[] = { "this", "toPush" };
		Type argTypes[] = { classType, TypeList.get(baseType) };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		ArrayList toPush = (ArrayList) csThread.getObject("toPush");
		list.addAll(toPush);
		return toPush;
	}
}
