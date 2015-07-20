package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string;

import ca.mcgill.mcb.pcingola.bigDataScript.data.Data;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

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
				) return ""; // Download error

		return array2list(Gpr.readFile(data.getLocalPath(), false).split("\n"));
	}
}
