package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import java.util.HashMap;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeMap;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

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
	protected Object runFunctionNative(BigDataScriptThread csThread) {
		String fileName = csThread.getString("file");
		HashMap<String, String> configOri = (HashMap<String, String>) csThread.getObject("configOri");
		return parseFile(csThread, fileName, configOri);
	}
}
