package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Tail: Create a new list with all the elements but the first
 *
 * @author pcingola
 */
public class MethodNativeListTail extends MethodNativeList {

	private static final long serialVersionUID = -4517146733140753324L;

	public MethodNativeListTail(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "tail";
		returnType = classType;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		// Empty list? => Nothing to do
		ValueList tl = new ValueList(vthis.getType());
		if (vthis.size() <= 1) return tl;

		// Add all but first elements from list
		int max = vthis.size();
		for (int i = 1; i < max; i++)
			tl.add(vthis.getValue(i));
		return tl;
	}

}
