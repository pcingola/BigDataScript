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
 * Join all elements of a ins into a string (using a specified separator)
 *
 * @author pcingola
 */
public class MethodNativeListJoinStr extends MethodNativeListJoin {

	private static final long serialVersionUID = 4062371438805409127L;

	public MethodNativeListJoinStr(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "join";
		returnType = Types.STRING;

		String argNames[] = { "this", "separator" };
		Type argTypes[] = { classType, Types.STRING };

		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		String sep = bdsThread.getString("separator");
		return new ValueString(join(vthis, sep));
	}
}
