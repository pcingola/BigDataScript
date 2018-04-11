package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Add: Remove an element from the list
 *
 * @author pcingola
 */
public class MethodNativeListRemoveIdx extends MethodNativeList {

	private static final long serialVersionUID = -1053259928442546934L;

	public MethodNativeListRemoveIdx(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "removeIdx";
		returnType = baseType;

		String argNames[] = { "this", "idx" };
		Type argTypes[] = { classType, Types.INT };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		int idx = (int) bdsThread.getInt("idx");
		return vthis.remove(idx);
	}

}
