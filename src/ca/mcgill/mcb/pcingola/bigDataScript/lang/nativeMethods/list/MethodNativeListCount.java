package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

/**
 * Add: Remove an element from the list
 *
 * @author pcingola
 */
public class MethodNativeListCount extends MethodNativeList {

	public MethodNativeListCount(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "count";
		classType = TypeList.get(baseType);
		returnType = Type.INT;

		String argNames[] = { "this", "toCount" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		Object toCount = csThread.getObject("toCount");

		long count = 0;
		for (Object o : list)
			if (toCount.equals(o)) count++;

		return count;
	}
}
