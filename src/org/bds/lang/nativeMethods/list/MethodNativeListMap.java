package org.bds.lang.nativeMethods.list;

import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Map: Apply a function to all elements in the list
 *
 * @author pcingola
 */
public class MethodNativeListMap extends MethodNativeList {

	private static final long serialVersionUID = -4111423423440525687L;

	public MethodNativeListMap(TypeList listType) {
		super(listType);
	}

	@Override
	protected void initMethod(Type baseType) {
		// TODO : Not working, needs re-implementation
	}

	@Override
	public Value runMethod(BdsThread bdsThread, ValueList vthis) {
		// TODO: Functional style not implemented!
		throw new RuntimeException("Unimplemented!");
	}

}
