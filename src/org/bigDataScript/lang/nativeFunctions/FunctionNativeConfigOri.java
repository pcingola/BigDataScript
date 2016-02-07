package org.bigDataScript.lang.nativeFunctions;

import java.util.HashMap;

import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeMap;
import org.bigDataScript.run.BdsThread;

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
	protected Object runFunctionNative(BdsThread csThread) {
		String fileName = csThread.getString("file");
		HashMap<String, String> configOri = (HashMap<String, String>) csThread.getObject("configOri");
		return parseFile(csThread, fileName, configOri);
	}
}
