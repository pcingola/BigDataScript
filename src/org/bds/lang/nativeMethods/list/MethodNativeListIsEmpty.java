package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * List's length (number of elements)
 *
 * @author pcingola
 */
public class MethodNativeListIsEmpty extends MethodNativeList {

	private static final long serialVersionUID = 3783361619646858067L;

	public MethodNativeListIsEmpty(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "isEmpty";
		returnType = Types.BOOL;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		return new ValueBool(vthis.isEmpty());
	}
}
