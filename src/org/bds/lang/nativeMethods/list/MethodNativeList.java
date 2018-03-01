package org.bds.lang.nativeMethods.list;

import java.util.ArrayList;

import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.run.BdsThread;

/**
 * Methods for list/array/stack
 *
 * @author pcingola
 */
public abstract class MethodNativeList extends MethodNative {

	public MethodNativeList(Type baseType) {
		super();
		if (baseType != null) initMethod(baseType);
	}

	@Override
	protected void initMethod() {
		// Nothing to do, we cannot initialize directly
	}

	protected abstract void initMethod(Type baseType);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		Object toPush = bdsThread.getObject("toPush");
		list.add(toPush);
		return toPush;
	}
}
