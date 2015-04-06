package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string;

import java.io.File;
import java.io.IOException;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Exec;
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
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		// Get canonical path to directory
		String dirName = objThis.toString();
		File dir = new File(dirName);
		String path = null;

		try {
			path = dir.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException("Error trying to chdir to '" + path + "'", e);
		}

		// Re build directory using canonical path
		// WARNING: 
		//     If we don't do this, then relative paths are referenced
		//     to 'user.dir' at the beginning of Java program instead of using
		//     the current one. I'm not sure whether this is a bug in Java's
		//     libraries or a 'feature'
		dir = new File(path);

		// Sanity check
		if (!dir.exists()) throw new RuntimeException("Directory '" + path + "' does not exists");
		if (!dir.isDirectory()) throw new RuntimeException("Cannot chdir to '" + path + "', not a directory.");

		// OK change dir
		BigDataScriptThreads.getInstance().get().setCurrentDir(path);
		return System.setProperty(Exec.USER_DIR, path);
	}
}
