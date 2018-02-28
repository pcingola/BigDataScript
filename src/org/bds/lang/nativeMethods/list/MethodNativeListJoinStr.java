package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

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
		returnType = Types.STRING;

		String argNames[] = { "this", "separator" };
		Type argTypes[] = { classType, Types.STRING };

		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		String ext = csThread.getString("separator");
		return join(list, ext);
	}
}
