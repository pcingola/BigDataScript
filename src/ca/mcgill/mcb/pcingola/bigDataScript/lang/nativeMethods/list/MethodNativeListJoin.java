package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Join all elements of a ins into a string
 * 
 * @author pcingola
 */
public class MethodNativeListJoin extends MethodNativeList {

	public MethodNativeListJoin(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "join";
		classType = TypeList.get(baseType);
		returnType = Type.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings("rawtypes")
	String join(ArrayList list, String str) {
		StringBuilder sb = new StringBuilder();

		if (list.isEmpty()) return "";

		int max = list.size() - 1;
		for (int i = 0; i < max; i++)
			sb.append(list.get(i) + str);
		sb.append(list.get(max));

		return sb.toString();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		return join(list, " ");
	}
}
