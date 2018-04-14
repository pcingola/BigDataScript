package org.bds.lang.nativeMethods.string;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;
import org.bds.util.Gpr;

public class MethodNative_string_readLines extends MethodNativeString {

	private static final long serialVersionUID = -1493054981595216278L;

	public MethodNative_string_readLines() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "readLines";
		classType = Types.STRING;
		returnType = TypeList.get(Types.STRING);

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, Value vThis) {
		// Download data if necessary
		String fileName = vThis.asString();
		Data data = bdsThread.data(fileName);

		// Download remote file
		ValueList vlist = new ValueList(returnType);
		if (data.isRemote() //
				&& !data.isDownloaded() //
				&& !data.download() //
		) return vlist; // Download error

		// Local file doesn't exist? Return an empty list
		if (!Gpr.exists(data.getLocalPath())) return vlist;

		// Read file and split it
		// Note: If the file is empty, it should return a list with a single empty string (not an empty list)
		return arrayString2valuelist(Gpr.readFile(data.getLocalPath(), false).split("\n"));
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		throw new RuntimeException("This method should never be invoked!");
	}
}
