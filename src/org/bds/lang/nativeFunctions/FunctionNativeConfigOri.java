package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.lang.type.Types;
import org.bds.lang.value.ValueMap;
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
		returnType = TypeMap.get(Types.STRING, Types.STRING);

		String argNames[] = { "file", "configOri" };
		Type argTypes[] = { Types.STRING, returnType };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		String fileName = bdsThread.getString("file");
		ValueMap configOri = (ValueMap) bdsThread.getValue("configOri");
		return parseFile(bdsThread, fileName, configOri.get());
	}
}
