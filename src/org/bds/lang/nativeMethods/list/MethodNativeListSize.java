package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * List's length (number of elements)
 *
 * @author pcingola
 */
public class MethodNativeListSize extends MethodNativeList {

	private static final long serialVersionUID = 1605853473198421113L;

	public MethodNativeListSize(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "size";
		returnType = Types.INT;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		return new ValueInt((long) vthis.size());
	}
}
