package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Sort: Create a new list and reverse it
 *
 * @author pcingola
 */
public class MethodNativeListReverse extends MethodNativeList {

	private static final long serialVersionUID = -3872253997912952255L;

	public MethodNativeListReverse(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "reverse";
		returnType = TypeList.get(baseType);

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		// Empty list? => Nothing to reverse, return a new list
		ValueList rl = new ValueList(vthis.getType());
		if (vthis.size() <= 0) return rl;

		// Add all items and reverse list
		rl.addAll(vthis);
		rl.reverse();

		return rl;
	}
}
