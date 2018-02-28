package org.bds.lang.nativeFunctions;

import java.util.HashMap;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.run.BdsThread;

/**
 * Native function "config". Read a 'config' file and return values in a hash.
 * Use 'ori' values as defaults
 * 
 * @author pcingola
 */
public class FunctionNativeConfigOri extends FunctionNativeConfig {

	public FunctionNativeConfigOri() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "config";
		returnType = TypeMap.get(Type.STRING);

		String argNames[] = { "file", "configOri" };
		Type argTypes[] = { Type.STRING, TypeMap.get(Type.STRING) };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		String fileName = bdsThread.getString("file");
		HashMap<String, String> configOri = (HashMap<String, String>) bdsThread.getObject("configOri");
		return parseFile(bdsThread, fileName, configOri);
	}
}
