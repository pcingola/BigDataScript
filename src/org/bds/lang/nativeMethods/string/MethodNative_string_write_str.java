package org.bds.lang.nativeMethods.string;

import java.io.File;

import org.bds.data.Data;
import org.bds.data.DataRemote;
import org.bds.lang.Parameters;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.util.Gpr;

public class MethodNative_string_write_str extends MethodNativeString {
	public MethodNative_string_write_str() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "write";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this", "str" };
		Type argTypes[] = { Types.STRING, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		// Download data if nescesary
		Data data = bdsThread.data(objThis.toString());

		// Save to file
		String str = bdsThread.getString("str");
		if (data.isRemote()) {
			DataRemote dr = (DataRemote) data;
			if (!dr.isFile()) bdsThread.fatalError(this, "Cannot write to non-file: " + dr.getAbsolutePath());

			// Save to temp file and upload
			String tmpFileName = dr.getLocalPath();

			// Make sure temp dir exists
			(new File(tmpFileName)).getParentFile().mkdirs();

			// Create local file
			Gpr.debug("local file: " + tmpFileName);
			Gpr.toFile(tmpFileName, str);

			// Upload file
			if (!data.upload(tmpFileName)) return ""; // Failed upload?
		} else {
			// Save to local file
			Gpr.toFile(data.getLocalPath(), str);
		}

		// OK
		return str;
	}
}
