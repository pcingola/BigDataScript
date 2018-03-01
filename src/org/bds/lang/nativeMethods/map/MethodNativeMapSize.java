package org.bds.lang.nativeMethods.map;

import java.util.Map;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

/**
 * Return a list of keys
 * 
 * @author pcingola
 */
public class MethodNativeMapSize extends MethodNativeMap {

	public MethodNativeMapSize(TypeMap mapType) {
		super(mapType);
	}

	@Override
	protected void initMethod() {
		functionName = "size";
		classType = mapType;
		returnType = Types.INT;

		String argNames[] = { "this" };
		Type argTypes[] = { mapType };
		parameters = Parameters.get(argTypes, argNames);

		addNativeMethodToClassScope();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		Map map = (Map) objThis;
		return (long) map.size();
	}
}
