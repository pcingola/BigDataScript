package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueMap;
import org.bds.lang.value.ValueString;
import org.bds.run.BdsThread;
import org.bds.util.Gpr;

/**
 * Native function "config". Read a 'config' file and return values in a hash.
 *
 * @author pcingola
 */
public class FunctionNativeConfig extends FunctionNative {

	private static final long serialVersionUID = 5436763379885480214L;

	public FunctionNativeConfig() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "config";
		returnType = TypeMap.get(Types.STRING, Types.STRING);

		String argNames[] = { "file" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	protected ValueMap parseFile(BdsThread bdsThread, String fileName, ValueMap configOri) {
		// Sanity check
		if (!Gpr.canRead(fileName)) bdsThread.fatalError(this, "Cannot read config file '" + fileName + "'");

		// Create config, add default values
		ValueMap config = new ValueMap(TypeMap.get(Types.STRING, Types.STRING));
		if (configOri != null) {
			// Copy all values from 'configOri'
			for (Value k : configOri.keySet()) {
				config.put(k, configOri.getValue(k));
			}
		}

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
			if (!name.isEmpty()) config.put(new ValueString(name), new ValueString(value));
		}

		return config;
	}

	@Override
	public Value runFunction(BdsThread bdsThread) {
		String fileName = bdsThread.getString("file");
		return parseFile(bdsThread, fileName, null);
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		throw new RuntimeException("This method should never be invoked!");
	}

}
