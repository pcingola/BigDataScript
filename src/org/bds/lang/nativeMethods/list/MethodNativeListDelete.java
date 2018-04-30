package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Rm: Delete all files in list: same as string[].rm()
 *
 * @author pcingola
 */
public class MethodNativeListDelete extends MethodNativeList {

	private static final long serialVersionUID = -5096203121666642452L;

	public MethodNativeListDelete(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "delete";
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
