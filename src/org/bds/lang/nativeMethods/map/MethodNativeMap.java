package org.bds.lang.nativeMethods.map;

import java.util.ArrayList;

import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

/**
 * Methods for Maps
 * 
 * @author pcingola
 */
public abstract class MethodNativeMap extends MethodNative {

	public MethodNativeMap(Type mapType) {
		super();
		initMethod(mapType);
	}

	@Override
	protected void initMethod() {
		// Nothing to do, we cannot initialize directly
	}

	protected abstract void initMethod(Type mapType);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		Object toPush = bdsThread.getObject("toPush");
		list.add(toPush);
		return toPush;
	}
}
