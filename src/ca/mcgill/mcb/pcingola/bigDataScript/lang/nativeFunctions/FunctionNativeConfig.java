package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import java.util.HashMap;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeMap;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Native function "config". Read a 'config' file and return values in a hash.
 *
 * @author pcingola
 */
public class FunctionNativeConfig extends FunctionNative {

	public FunctionNativeConfig() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "config";
		returnType = TypeMap.get(Type.STRING);

		String argNames[] = { "file" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	protected Object parseFile(BigDataScriptThread csThread, String fileName, HashMap<String, String> configOri) {
		// Sanity check
		if (!Gpr.canRead(fileName)) csThread.fatalError(this, "Cannot read config file '" + fileName + "'");

		// Create config, add default values
		HashMap<String, String> config = new HashMap<>();
		if (configOri != null) config.putAll(configOri);

		// Read and parse file
		String fileContents = Gpr.readFile(fileName);
		for (String line : fileContents.split("\n")) {
			line = line.trim();
			if (line.startsWith("#")) continue;

			String name = "";
			String value = "";

			// Split by either ':', '=' or '\t' (whatever is found first)
			int idx1 = line.indexOf(':');
			int idx2 = line.indexOf('=');
			int idx3 = line.indexOf('\t');

			if (idx1 < 0) idx1 = Integer.MAX_VALUE;
			if (idx2 < 0) idx2 = Integer.MAX_VALUE;
			if (idx3 < 0) idx3 = Integer.MAX_VALUE;

			int idx = Math.min(idx1, Math.min(idx2, idx3));

			if (idx < line.length()) {
				name = line.substring(0, idx);
				value = line.substring(idx + 1);
			}

			// Trim and add to map
			name = name.trim();
			value = value.trim();
			if (!name.isEmpty()) config.put(name, value);
		}

		return config;
	}

	@Override
	protected Object runFunctionNative(BigDataScriptThread csThread) {
		String fileName = csThread.getString("file");
		return parseFile(csThread, fileName, null);
	}
}
