package org.bds.lang.nativeMethods.list;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Add: Add an element to the end of the list
 *
 * Note: Exactly the same as push
 *
 * @author pcingola
 */
public class MethodNativeListClone extends MethodNativeList {

	private static final long serialVersionUID = 3597972266464998846L;

	public MethodNativeListClone(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		functionName = "clone";
		returnType = classType;

		String argNames[] = { "this" };
		Type argTypes[] = { classType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		ValueList newList = new ValueList(vthis.getType());
		newList.addAll(vthis);
		return newList;
	}
}
