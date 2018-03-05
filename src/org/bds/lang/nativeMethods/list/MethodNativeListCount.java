package org.bds.lang.nativeMethods.list;

import java.util.List;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;

/**
 * Add: Remove an element from the list
 *
 * @author pcingola
 */
public class MethodNativeListCount extends MethodNativeList {

	public MethodNativeListCount(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "count";
		returnType = Types.INT;

		String argNames[] = { "this", "toCount" };
		Type argTypes[] = { classType, baseType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		List list = (List) objThis;
		Value toCount = bdsThread.getValue("toCount");
		Object oToCount = toCount.get();

		long count = 0;
		for (Object o : list)
			if (oToCount.equals(o)) count++;

		return count;
	}
}
