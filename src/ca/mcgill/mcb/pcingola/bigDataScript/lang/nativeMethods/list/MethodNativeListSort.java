package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;
import java.util.Collections;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

/**
 * Sort: Create a new list and sort it
 * 
 * @author pcingola
 */
public class MethodNativeListSort extends MethodNativeList {

	public MethodNativeListSort(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "sort";
		classType = TypeList.get(baseType);
		returnType = TypeList.get(baseType);

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;

		// Empty list? => Nothing to do
		if (list.size() <= 0) return new ArrayList();

		// Create new list and sort it
		ArrayList newList = new ArrayList(list.size());
		newList.addAll(list);
		Collections.sort(newList);

		return newList;
	}
}
