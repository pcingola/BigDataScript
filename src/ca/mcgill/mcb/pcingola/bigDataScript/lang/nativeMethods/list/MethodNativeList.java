package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Add: Add an element to the end of the list
 * 
 * Note: Exactly the same as push
 * 
 * @author pcingola
 */
public abstract class MethodNativeList extends MethodNative {

	public MethodNativeList(Type baseType) {
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
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		ArrayList list = (ArrayList) objThis;
		Object toPush = csThread.getObject("toPush");
		list.add(toPush);
		return toPush;
	}
}
