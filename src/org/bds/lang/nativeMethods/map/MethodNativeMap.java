package org.bds.lang.nativeMethods.map;

import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.TypeMap;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueMap;
import org.bds.run.BdsThread;

/**
 * Methods for Maps
 *
 * @author pcingola
 */
public abstract class MethodNativeMap extends MethodNative {

	private static final long serialVersionUID = 7615366600665164582L;

	public MethodNativeMap(TypeMap mapType) {
		super(mapType);
	}

	@Override
	protected void initMethod() {
		// Nothing to do, we cannot initialize directly
	}

	@Override
	public Value runMethod(BdsThread bdsThread, Value vthis) {
		return runMethodNative(bdsThread, (ValueMap) vthis);
	}

	protected abstract Value runMethodNative(BdsThread bdsThread, ValueMap vthis);

}
