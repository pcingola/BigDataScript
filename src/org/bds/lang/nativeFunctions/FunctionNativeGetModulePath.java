package org.bds.lang.nativeFunctions;

import java.io.File;

import org.bds.lang.BdsNode;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

/**
 * Get a path to current code
 *
 * @author pcingola
 */
public class FunctionNativeGetModulePath extends FunctionNative {

	private static final long serialVersionUID = 6415943745404236449L;

	public FunctionNativeGetModulePath() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "getModulePath";
		returnType = Types.STRING;

		String argNames[] = {};
		Type argTypes[] = {};
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		BdsNode n = bdsThread.getBdsNodeCurrent();
		if (n == null) return "";
		File f = n.getFile();
		if (f == null) return "";
		return f.getAbsolutePath();
	}

}
