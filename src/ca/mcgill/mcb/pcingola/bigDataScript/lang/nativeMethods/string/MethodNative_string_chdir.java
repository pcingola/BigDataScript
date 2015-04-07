package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string;

import java.io.File;
import java.io.IOException;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThreads;

public class MethodNative_string_chdir extends MethodNative {

	public MethodNative_string_chdir() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "chdir";
		classType = Type.STRING;
		returnType = Type.VOID;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BigDataScriptThread bdsThread, Object objThis) {
		String dirName = objThis.toString();
		File dir = bdsThread.file(dirName);

		String path = null;
		try {
			path = dir.getCanonicalPath();
		} catch (IOException e) {
			bdsThread.fatalError(this, "Cannot resolve directory '" + dir + "'");
		}

		if (!dir.exists()) throw new RuntimeException("Directory '" + path + "' does not exists");
		if (!dir.isDirectory()) throw new RuntimeException("Cannot chdir to '" + path + "', not a directory.");

		// OK change dir
		BigDataScriptThreads.getInstance().get().setCurrentDir(path);
		return null;
	}
}
