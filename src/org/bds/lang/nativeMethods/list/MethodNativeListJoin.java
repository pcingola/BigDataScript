package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.lang.value.ValueString;
import org.bds.run.BdsThread;

/**
 * Join all elements of a ins into a string
 *
 * @author pcingola
 */
public class MethodNativeListJoin extends MethodNativeList {

	private static final long serialVersionUID = -7656495317502040552L;

	public MethodNativeListJoin(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "join";
		returnType = Types.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	String join(ValueList list, String str) {
		StringBuilder sb = new StringBuilder();

		if (list.isEmpty()) return "";

		int max = list.size() - 1;
		for (int i = 0; i < max; i++)
			sb.append(list.getValue(i).asString() + str);
		sb.append(list.getValue(max).asString());

		return sb.toString();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		return new ValueString(join(vthis, " "));
	}
}
