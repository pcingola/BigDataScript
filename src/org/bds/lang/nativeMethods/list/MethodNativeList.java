package org.bds.lang.nativeMethods.list;

import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

/**
 * Methods for list/array/stack
 *
 * @author pcingola
 */
public abstract class MethodNativeList extends MethodNative {

	private static final long serialVersionUID = 488512750560356752L;
	Type elementType;

	public MethodNativeList(TypeList listType) {
		super(listType);
		if (listType != null) {
			elementType = listType.getElementType();
			initMethod(elementType);
		}
		parameterNames = parameterNames();
	}

	@Override
	protected void initMethod() {
		// Nothing to do, we cannot initialize directly
	}

	/**
	 * Initialzie according to list's elements type (a.k.a. baseType)
	 * @param baseType
	 */
	protected abstract void initMethod(Type baseType);

	@Override
	public Value runMethod(BdsThread bdsThread, Value vthis) {
		return runMethod(bdsThread, (ValueList) vthis);
	}

	public abstract Value runMethod(BdsThread bdsThread, ValueList vthis);

}
