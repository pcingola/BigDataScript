package org.bds.lang.nativeMethods.map;

import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.TypeMap;

/**
 * Methods for Maps
 * 
 * @author pcingola
 */
public abstract class MethodNativeMap extends MethodNative {

	TypeMap mapType;

	public MethodNativeMap(TypeMap mapType) {
		super(mapType);
		this.mapType = mapType;
		initMethod();
	}

	@Override
	protected void initMethod() {
		// Nothing to do, we cannot initialize directly
	}

}
