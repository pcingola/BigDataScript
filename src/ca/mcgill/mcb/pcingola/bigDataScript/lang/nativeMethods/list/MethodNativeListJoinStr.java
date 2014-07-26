package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Join all elements of a ins into a string (using a specified separator)
 * 
 * @author pcingola
 */
public class MethodNativeListJoinStr extends MethodNativeListJoin {

	public MethodNativeListJoinStr(Type baseType) {
		super(baseType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "join";
		classType = TypeList.get(baseType);
		returnType = Type.STRING;

		String argNames[] = { "this", "separator" };
		Type argTypes[] = { classType, Type.STRING };

		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		String ext = csThread.getString("separator");
		return join(list, ext);
	}
}
