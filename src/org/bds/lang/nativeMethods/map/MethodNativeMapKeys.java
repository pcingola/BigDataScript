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
 * Return a list of keys
 *
 * @author pcingola
 */
public class MethodNativeMapKeys extends MethodNativeMap {

	private static final long serialVersionUID = -1528668670882984730L;

	public MethodNativeMapKeys(TypeMap mapType) {
		super(mapType);
	}

	@Override
	protected void initMethod() {
		TypeMap mapType = (TypeMap) classType;
		functionName = "keys";
		returnType = TypeList.get(mapType.getKeyType());

		String argNames[] = { "this" };
		Type argTypes[] = { mapType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@Override
	protected Value runMethodNative(BdsThread bdsThread, ValueMap vthis) {
		TypeMap tm = (TypeMap) vthis.getType();
		TypeList tl = TypeList.get(tm.getKeyType());
		ValueList vlist = new ValueList(tl);
		vlist.addAll(vthis.keySet());
		vlist.sort();
		return vlist;
	}
}
