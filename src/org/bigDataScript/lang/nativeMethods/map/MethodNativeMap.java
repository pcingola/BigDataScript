package org.bigDataScript.lang.nativeMethods.map;

import java.util.ArrayList;

import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeMethods.MethodNative;
import org.bigDataScript.run.BdsThread;

/**
 * Methods for Maps
 * 
 * @author pcingola
 */
public abstract class MethodNativeMap extends MethodNative {

	public MethodNativeMap(Type baseType) {
		super();
		initMethod(baseType);
	}

	@Override
	protected void initMethod() {
		// Nothing to do, we cannot initialize directly
	}

	protected abstract void initMethod(Type baseType);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		Object toPush = csThread.getObject("toPush");
		list.add(toPush);
		return toPush;
	}
}
