package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

/**
 * Add: Add an element to the list (position 'idx')
 * 
 * @author pcingola
 */
public class MethodNativeListAddIndex extends MethodNativeList {

	public MethodNativeListAddIndex(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "add";
		classType = TypeList.get(baseType);
		returnType = baseType;

		String argNames[] = { "this", "idx", "toPush" };
		Type argTypes[] = { classType, Type.INT, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		long idx = csThread.getInt("idx");
		Object toPush = csThread.getObject("toPush");
		list.add((int) idx, toPush);
		return toPush;
	}
}
