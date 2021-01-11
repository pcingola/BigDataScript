package org.bds.lang.nativeMethods.string;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_chdir extends MethodNativeString {

	private static final long serialVersionUID = 2008621469747150848L;

	public MethodNative_string_chdir() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "chdir";
		classType = Types.STRING;
		returnType = Types.VOID;

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
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
		bdsThread.setCurrentDir(path);
		return null;
	}
}
