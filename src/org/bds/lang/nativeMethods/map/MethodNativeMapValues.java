package org.bds.lang.nativeMethods.map;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.TypeMap;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.lang.value.ValueMap;
import org.bds.run.BdsThread;

/**
 * Return a list of values
 *
 * @author pcingola
 */
public class MethodNativeMapValues extends MethodNativeMap {

	private static final long serialVersionUID = -8674758888911074901L;

	public MethodNativeMapValues(TypeMap mapType) {
		super(mapType);
	}

	@Override
	protected void initMethod() {
		TypeMap mapType = (TypeMap) classType;
		functionName = "values";
		returnType = TypeList.get(mapType.getValueType());

		String argNames[] = { "this" };
		Type argTypes[] = { mapType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	protected Value runMethodNative(BdsThread bdsThread, ValueMap vthis) {
		TypeMap tm = (TypeMap) vthis.getType();
		TypeList tl = TypeList.get(tm.getValueType());
		ValueList vlist = new ValueList(tl);
		vlist.addAll(vthis.values());
		vlist.sort();
		return vlist;
	}

}
