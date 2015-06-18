package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Add: Remove an element from the list
 *
 * @author pcingola
 */
public class MethodNativeListRemoveIdx extends MethodNativeList {

	public MethodNativeListRemoveIdx(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "removeIdx";
		classType = TypeList.get(baseType);
		returnType = Type.INT;

		String argNames[] = { "this", "idx" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		long idx = csThread.getInt("idx");
		list.remove((int) idx);
		return idx;
	}
}
