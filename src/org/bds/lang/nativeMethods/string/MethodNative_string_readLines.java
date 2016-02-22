package org.bds.lang.nativeMethods.string;

import java.util.ArrayList;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.TypeList;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.run.BdsThread;
import org.bds.util.Gpr;

public class MethodNative_string_readLines extends MethodNative {
	public MethodNative_string_readLines() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "readLines";
		classType = Type.STRING;
		returnType = TypeList.get(Type.STRING);

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		// Download data if necessary
		Data data = bdsThread.data(objThis.toString());

		// Download remote file
		if (data.isRemote() //
				&& !data.isDownloaded() //
				&& !data.download() //
		) return new ArrayList<String>(); // Download error

		// Local file doesn't exist? Return an empty list
		if (!Gpr.exists(data.getLocalPath())) return new ArrayList<String>();

		// Read file and split it
		// Note: If the file is empty, it should return a list with a single empty string (not an empty list)
		return array2list(Gpr.readFile(data.getLocalPath(), false).split("\n"));
	}
}
