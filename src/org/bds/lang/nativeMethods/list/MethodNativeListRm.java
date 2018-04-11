package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Rm: Delete all files in list
 *
 * @author pcingola
 */
public class MethodNativeListRm extends MethodNativeList {

	private static final long serialVersionUID = 2756073526987367599L;

	public MethodNativeListRm(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "rm";
		returnType = Types.VOID;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		for (Value v : vthis)
			bdsThread.data(v.asString()).delete();
		return vthis;
	}
}
