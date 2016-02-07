package org.bigDataScript.lang.nativeMethods.string;

import org.bigDataScript.data.Data;
import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.nativeMethods.MethodNative;
import org.bigDataScript.run.BdsThread;
import org.bigDataScript.run.BdsThreads;

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
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String dirName = objThis.toString();
		Data dir = bdsThread.data(dirName);

		// Is it remote?
		if (dir.isRemote()) throw new RuntimeException("Cannot chdir to remote directory '" + dirName + "'");

		// Local dir processing
		String path = dir.getPath();
		if (!dir.exists()) throw new RuntimeException("Directory '" + path + "' does not exists");
		if (!dir.isDirectory()) throw new RuntimeException("Cannot chdir to '" + path + "', not a directory.");

		// OK change dir
		BdsThreads.getInstance().get().setCurrentDir(path);
		return null;
	}
}
